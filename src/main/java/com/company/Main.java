package com.company;

import java.io.IOException;

import java.net.URL;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

public class Main {

    private static final int NUMBER_OF_THREADS = 5;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        List<URL> urls = new ArrayList<>();
        if(args.length < 1) {
            System.err.println("Please specify at least 1 URL");
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
        long start = System.currentTimeMillis();
        for(URL url : urls) {
            ReaderThread rt = new ReaderThread(url);
            rt.start();
        }
        System.out.printf("Elapsed time:%d%n", System.currentTimeMillis() - start);
    }

    private static void javaMultiThread(List<URL> urls) {
        for(URL url : urls) {
            ReaderThread rt = new ReaderThread(url);
            rt.start();
        }
    }

    private static void futureSingleThread(List<URL> urls) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        for (URL url : urls) {
            executor.submit(new ReaderCallable(url)).get();
        }
        executor.shutdown();
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("Future single thread: " + elapsedTimeMillis + "ms passed");
    }

    private static void futureMultiThread(List<URL> urls) {
        long start = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future<String>> futures = new ArrayList<>();

        for(URL url : urls) {
            futures.add(pool.submit(new ReaderCallable(url)));
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
