package com.company;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReaderThread extends Thread {
    URL urls[] = new URL[1];

    public ReaderThread(URL urls[]) {
        this.urls = urls;
    }

    public ReaderThread(URL url) {
        this.urls[0] = url;
    }

    public void run() {
        long start = System.currentTimeMillis();

        for(int i = 0; i < urls.length; i++) {
            try {
                HttpURLConnection con = (HttpURLConnection) urls[i].openConnection();
                System.out.println(urls[i].toString() + ":  " + con.getResponseCode() + "  " + con.getResponseMessage());
                con.disconnect();
            } catch(IOException e) {
                System.out.println(e);
            }
        }

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Java thread: " + elapsedTimeMillis + "ms passed");
    }
}
