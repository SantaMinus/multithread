package com.company;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

public class Main {

    private static final int NUMBER_OF_THREADS = 5;

    public static void main(String[] args) throws IOException {
        List<URL> urls = new ArrayList<>();
        if(args.length < 1) {
            System.err.println("Please specify 5 URLs");
            return;
        }
        for(String arg : args) {
            urls.add(new URL(arg));
        }

        javaSingleThread(urls);
        javaMultiThread(urls);
        futureSingleThread(urls);
        futureMultiThread(urls);
    }

    private static void javaSingleThread(List<URL> urls) throws IOException {
        ReaderThread rt = new ReaderThread(urls);
        rt.start();
    }

    private static void javaMultiThread(List<URL> urls) {
        for(URL url : urls) {
            ReaderThread rt = new ReaderThread(url);
            rt.start();
        }
    }

    private static void futureSingleThread(List<URL> urls) {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        Future<String> future;

        for(URL url : urls) {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws IOException {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    return url.toString() + ": " + con.getResponseCode() + "  " + con.getResponseMessage();
                }
            };
            future = executor.submit(callable);
            try {
                String str = future.get();
                System.out.println(str);
            } catch (ExecutionException ee) {
                System.err.println("Callable through exception: " + ee.getMessage());
            } catch (InterruptedException e) {
                System.err.println("URL not responding");
            }
        }
        executor.shutdown();

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Future single thread: " + elapsedTimeMillis + "ms passed");
    }

    private static void futureMultiThread(List<URL> urls) {
        long start = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Future<String>> futures = new ArrayList<>();

        for(URL url : urls) {
            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws IOException {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    return url.toString() + ": " + con.getResponseCode() + "  " + con.getResponseMessage();
                }
            };
            futures.add(pool.submit(task));
        }
        for(Future<String> future : futures) {
            try {
                String str = future.get();
                System.out.println(str);
            } catch (ExecutionException ee) {
                System.err.println("Callable through exception: " + ee.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Future multi thread: " + elapsedTimeMillis + "ms passed");

        pool.shutdown();
    }
}
