package com.company;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ReaderThread extends Thread {
    private List<URL> urls;

    ReaderThread(List<URL> urls) {
        this.urls = urls;
    }

    ReaderThread(URL url) {
        this.urls = new ArrayList<>();
        this.urls.add(url);
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        for (URL url : urls) {
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                System.out.format("%s  %s:  %d %s\n",
                        Thread.currentThread().getName(), url.toString(), con.getResponseCode(), con.getResponseMessage());
                con.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Java thread: " + elapsedTimeMillis + "ms passed");
    }
}
