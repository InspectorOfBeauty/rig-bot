package com.village.bot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class ContentLoader {

    public static String loadContent(String url, Charset charset) throws IOException {
        BufferedReader bufferedReader;
        InputStream inputStream;
        HttpURLConnection httpURLConnection = null;

        try {
            URL address = new URL(url);
            httpURLConnection = (HttpURLConnection) address.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != 200) {
                throw new RuntimeException("Status code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
