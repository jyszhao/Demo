package com.zhaoy.java;

import java.util.concurrent.*;


/**
 * 第4种 获得/使用java多线程的方式，线程池
 */
public class MyThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());

        try{
            // 模拟 个用户来办理业务，每个用户就是一个来自外部的请求线程
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t 办理业务...");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }

    }

    public static void threadPoolInit() {
        //        System.out.println(Runtime.getRuntime().availableProcessors());

        // Array  Arrays
        // Collection  Collections
        // Executor  Executors
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);  //一池5个处理线程。
//        ExecutorService threadPool = Executors.newSingleThreadExecutor(); //一池1个处理线程。
        ExecutorService threadPool = Executors.newCachedThreadPool(); //一池N个处理线程。

        try{
            // 模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+"\t 办理业务");
                });
                /*// CachedThreadPool N个线程 暂停一会儿
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
