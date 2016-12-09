package com.company;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ReaderThread extends Thread {
    private URL url;

    ReaderThread(URL url) {
        this.url = Objects.requireNonNull(url, "URL should not be null");
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        HttpURLConnection con = null;

        try {
            con = (HttpURLConnection) url.openConnection();
            System.out.format("%s  %s:  %d %s\n",
                    Thread.currentThread().getName(), url.toString(), con.getResponseCode(), con.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(con != null)
                con.disconnect();
        }

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Java thread: " + elapsedTimeMillis + "ms passed");
    }
}
