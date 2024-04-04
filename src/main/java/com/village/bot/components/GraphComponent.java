package com.village.bot.components;

import com.village.bot.entities.RequestStatus;
import com.village.bot.entities.RigData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static com.village.bot.util.HashUtil.hashToMegaHash;
import static com.village.bot.view.GraphText.*;

@Component
@RequiredArgsConstructor
public class GraphComponent {

    public void loadGraph(List<RigData> history, LocalDateTime now, Consumer<InputStream> processor) throws IOException {
        HttpURLConnection httpURLConnection = null;
        String body = generateBody(history, now);

        try {
            URL address = new URL("https://quickchart.io/chart");
            httpURLConnection = (HttpURLConnection) address.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.setRequestProperty("Content-Length", Integer.toString(body.length()));
            httpURLConnection.setDoOutput(true); // Для отправки контента

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(body.getBytes(StandardCharsets.UTF_8), 0, body.length());
            }

            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != 200) {
                throw new RuntimeException("Status code is " + httpURLConnection.getResponseCode());
            }

            processor.accept(httpURLConnection.getInputStream());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public String generateBody(List<RigData> history, LocalDateTime now) {
        Map<String, List<String>> graphicData = serializeGraphicData(history, now);

        String time = graphicData.get("time").toString().replace(" ", "");
        String current = graphicData.get("current").toString().replace(" ", "");
        String for24Hours = graphicData.get("for24Hours").toString().replace(" ", "");
        String chartConfig = serializeChartConfiguration(time, current, for24Hours);

        return serializeBody("png", chartConfig);
    }

    public Map<String, List<String>> serializeGraphicData(List<RigData> history, LocalDateTime now) {
        Map<Integer, List<RigData>> dataForHour = new LinkedHashMap<>();

        int currentHour = now.getHour();

        for (int hour = currentHour + 1; hour < 24; hour++) {
            dataForHour.put(hour, new ArrayList<>());
        }
        for (int hour = 0; hour <= currentHour; hour++) {
            dataForHour.put(hour, new ArrayList<>());
        }

        for (RigData rigData : history) {
            if (rigData.getDataTime().getHour() == currentHour && rigData.getDataTime().getDayOfMonth() != now.getDayOfMonth()) {
                // пропустить вчерашние данные, у которых час совпадает с текущим часом
                continue;
            }

            int hour = rigData.getDataTime().getHour();
            dataForHour.get(hour).add(rigData);
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("time", new ArrayList<>());
        result.put("current", new ArrayList<>());
        result.put("for24Hours", new ArrayList<>());

        for (Map.Entry<Integer, List<RigData>> entry : dataForHour.entrySet()) {
            final List<RigData> hourData = entry.getValue();

            BigDecimal hourSum = hourData.stream()
                    .filter( data -> data.getStatus() == RequestStatus.RIG_ONLINE)
                    .map(RigData::getHashRate)
                    .reduce(BigDecimal::add)
                    .orElse(null);

            BigDecimal hour24H = hourData.stream()
                    .filter( data -> data.getStatus() == RequestStatus.RIG_ONLINE)
                    .max(Comparator.comparing(RigData::getDataTime))
                    .map(RigData::getDayHashRate)
                    .orElse(null);

            final BigDecimal hourAvg;
            if (hourSum != null) {
                hourAvg = hourSum.divide(BigDecimal.valueOf(hourData.size()), RoundingMode.CEILING);
            } else {
                hourAvg = null;
            }

            int hourToPrint = entry.getKey() + 1;
            addGraphicData(result, hourToPrint, hourAvg, hour24H);
        }

        return result;
    }

    public void addGraphicData(Map<String, List<String>> result, int hour, BigDecimal forHour, BigDecimal for24Hours) {
        String currentString;
        String for24HourString;

        if (forHour != null) {
            currentString = String.format("%.0f", hashToMegaHash(forHour));
        } else {
            currentString = "null";
        }

        if (for24Hours != null) {
            for24HourString = String.format("%.0f", hashToMegaHash(for24Hours));
        } else {
            for24HourString = "null";
        }

        result.get("time").add(String.format("'%02d:00'", hour));
        result.get("current").add(currentString);
        result.get("for24Hours").add(for24HourString);
    }

    public static String serializeChartConfiguration(String labels, String data1Hour, String data24Hour) {
        return String.format("{" +
                "type:'line'," +
                "data:{" +
                "labels:%s," +
                "datasets:[" +
                "{label:'%s',data:%s,fill:false,backgroundColor:'red',borderColor:'red'}," +
                "{label:'%s',data:%s,fill:false,backgroundColor:'green',borderColor:'green'}" +
                "]" +
                "}," +
                "options:{" +
                "title:{" +
                "display:true," +
                "text:'%s'" +
                "}" +
                "}" +
                "}", labels, toEscapedUnicode(LEGEND_1H), data1Hour, toEscapedUnicode(LEGEND_24H), data24Hour, toEscapedUnicode(GRAPH_HEADER));
    }

    public static String serializeBody(String imageFormat, String chartConfiguration) {
        return String.format("""
                {
                  "version": "2",
                  "backgroundColor": "white",
                  "width": 500,
                  "height": 300,
                  "devicePixelRatio": 1.0,
                  "format": "%s",
                  "chart": "%s"
                }""", imageFormat, chartConfiguration);
    }

    private static String toEscapedUnicode(String text) {
        StringBuilder b = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= 128)
                b.append("\\u").append(String.format("%04X", (int) c));
            else
                b.append(c);
        }

        return b.toString();
    }
}
