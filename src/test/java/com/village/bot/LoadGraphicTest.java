package com.village.bot;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoadGraphicTest {
    public static void main(String[] args) throws IOException {
        String time = "['16:00','17:00','18:00','19:00','20:00', '21:00', '22:00', '23:00', '00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00','12:00','13:00','14:00','15:00']";
        String current = "['144.00','182.00','163.00','163.00','172.00','144.00','182.00','163.00','163.00','172.00','144.00','182.00','163.00','163.00','172.00','144.00','182.00','163.00','163.00','172.00','144.00','182.00','163.00','163.00']";
        String for24Hours = "['151.00','150.00','151.00','150.00','148.00','151.00','150.00','151.00','150.00','148.00','151.00','150.00','151.00','150.00','148.00','151.00','150.00','151.00','150.00','148.00','151.00','150.00','151.00','150.00']";
        String chartConfig = formatChartConfiguration(time, current, for24Hours);

        String body = formatBody("png", chartConfig);

        String path = "C:\\Users\\alex\\IdeaProjects\\village-bot\\";
        String fileName = "graphic";
        loadGraphic(body, path, fileName);
    }

    public static String formatChartConfiguration(String labels, String data1, String data2) {
        return String.format("{" +
                    "type:'line'," +
                    "data:{" +
                        "labels:%s," +
                        "datasets:[" +
                            "{label:'Current',data:%s,fill:false,backgroundColor:'red',borderColor:'red'}," +
                            "{label:'24H',data:%s,fill:false,backgroundColor:'green',borderColor:'green'}" +
                        "]" +
                    "}," +
                    "options:{" +
                        "title:{" +
                        "display:true," +
                        "text:'Hash rate MH/s'" +
                        "}" +
                    "}" +
                "}", labels, data1, data2);
    }

    public static String formatBody(String format, String chartConfiguration) {
        return String.format("""
                {
                  "version": "2",
                  "backgroundColor": "white",
                  "width": 500,
                  "height": 300,
                  "devicePixelRatio": 1.0,
                  "format": "%s",
                  "chart": "%s"
                }""", format, chartConfiguration);
    }

    public static void loadGraphic(String body, String path, String fileName) throws IOException {
        HttpURLConnection httpURLConnection = null;

        try {
            URL address = new URL("https://quickchart.io/chart");
            httpURLConnection = (HttpURLConnection) address.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.setRequestProperty("Content-Length", Integer.toString(body.length()));
            httpURLConnection.setDoOutput(true); // Для отправки контента

            try(OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(body.getBytes(StandardCharsets.UTF_8), 0, body.length());
            }

            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != 200) {
                throw new RuntimeException("Status code is " + httpURLConnection.getResponseCode());
            }

            File targetFile = new File(path + fileName+".png");
            FileUtils.copyInputStreamToFile(httpURLConnection.getInputStream(), targetFile);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
