package com.example.shoppingCart.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class JsonCallable implements Callable<String> {
    private final String TAG = "TAG_JsonCallable";
    private final String url;
    private final String outStr;

    public JsonCallable(String url, String outStr) {
        this.url = url;
        this.outStr = outStr;
    }

    @Override
    public String call() {
        return getData();
    }

    public String getData() {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // 允許輸入
            connection.setDoOutput(true); // 允許輸出
            connection.setChunkedStreamingMode(0); // 不確定內容大小使用此方法做分段傳輸，0為預設大小
            connection.setUseCaches(false); // 不要cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
                bw.write(outStr);
                Log.e(TAG, "output: " + outStr);
            }

            int responseCode = connection.getResponseCode();
            Log.e(TAG, "response code: " + responseCode);

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        inStr.append(line);
                    }
                }
            } else {
                Log.e(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.e(TAG, "input: " + inStr);
        return inStr.toString();
    }
}
