package com.company;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class ReaderThread extends Thread {
    private URL urls[] = new URL[1];

    ReaderThread(URL urls[]) {
        this.urls = urls;
    }

    ReaderThread(URL url) {
        this.urls[0] = url;
    }

    public void run() {
        long start = System.currentTimeMillis();

        for (URL url : urls) {
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                System.out.println(this.getName() + "  " + url.toString() + ":  " + con.getResponseCode() + "  " + con.getResponseMessage());
                con.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Java thread: " + elapsedTimeMillis + "ms passed");
    }
}
