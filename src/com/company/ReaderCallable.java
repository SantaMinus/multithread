package com.company;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ReaderCallable implements Callable<String> {

    private final URL url;

    public ReaderCallable(URL url) {
        this.url = Objects.requireNonNull(url, "url should not be null");
    }

    @Override
    public String call() {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            return url.toString() + ": " + con.getResponseCode() + "  " + con.getResponseMessage();
        } catch (IOException e) {
            System.err.print(e.getMessage());
            return e.getMessage();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}