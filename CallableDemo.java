package com.zhaoy.java;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


class MyThread implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println("***********come in Callable");
        return 1024;
    }
}



public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Thread(Runnable target, String name)
        //  Allocates a new Thread object.

        //  FutureTask(Callable<V> callable)
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());

        Thread t1 = new Thread(futureTask, "AA");
        t1.start();

        System.out.println("***********result :"+futureTask.get());

    }

}
