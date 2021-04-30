package com.example.shoppingcart.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ImageCallable implements Callable<Bitmap> {
    private static final String TAG = "TAG_ImageCallable";
    private final String url;
    private final String outStr;

    public ImageCallable(String url, String outStr) {
        this.url = url;
        this.outStr = outStr;
    }

    @Override
    public Bitmap call() {
        return getImage();
    }

    public Bitmap getImage() {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // 允許輸入
            connection.setDoOutput(true); // 允許輸出
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.e(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                bitmap = BitmapFactory.decodeStream(new BufferedInputStream(connection.getInputStream()));
            } else {
                Log.e(TAG, "response code: " +  responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }
}
