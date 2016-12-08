package com.company;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws IOException {
        URL urls[] = new URL[5];
        if(args.length < 1) {
            System.err.println("Please specify 5 URLs");
            return;
        }
        for(int i=0; i< 5; i++) {
            urls[i] = new URL(args[i]);
        }

        javaSingleThread(urls);
        javaMultiThread(urls);
        futureSingleThread(urls);
        futureMultiThread(urls);
    }

    private static void javaSingleThread(URL urls[]) throws IOException {
        ReaderThread rt = new ReaderThread(urls);
        rt.start();
    }

    private static void javaMultiThread(URL urls[]) {
        for(int i = 0; i < 5; i++) {
            ReaderThread rt = new ReaderThread(urls[i]);
            rt.start();
        }
    }

    private static void futureSingleThread(URL urls[]) {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newSingleThreadExecutor();;
        Future<String> future;

        for(int i = 0; i < 5; i++) {
            final URL url = urls[i];

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

    private static void futureMultiThread(URL urls[]) {
        long start = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        Future<String> future = null;
        Object res;

        for(int i = 0; i < 5; i++) {
            final URL url = urls[i];

            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws IOException {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    return url.toString() + ": " + con.getResponseCode() + "  " + con.getResponseMessage();
                }
            };
            future = pool.submit(task);
            try {
                String str = future.get();
                System.out.println(str);
            } catch (ExecutionException ee) {
                System.err.println("Callable through exception: " + ee.getMessage());
            } catch (InterruptedException e) {
                System.err.println("URL not responding");
            }
        }

        /*while (!future.isDone()) {
            System.out.println("Waiting..");
            try {
                Thread.sleep(10); //sleep for 10 milliseconds before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Future multi thread: " + elapsedTimeMillis + "ms passed");

        pool.shutdown();
    }
}
