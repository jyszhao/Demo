package com.zhaoy.java;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  题目：多线程之间按顺序调用，实现A->B->C三个线程启动，要求如下：
 *  AA打印5次，BB打印10次，CC打印15次
 *  紧接着
 *  AA打印5次，BB打印10次，CC打印15次
 *  ......
 *  来10轮
 */
public class SyncAndReentrantLockDemo {
    public static void main(String[] args) {

        ShareResource shareResource = new ShareResource();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                shareResource.print5();
            }

        } , "A").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                shareResource.print10();
            }

        } , "B").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                shareResource.print15();
            }

        } , "C").start();

    }
}



class ShareResource{
    private int number = 1;  //A:1  B:2  C:3

    private Lock lock = new ReentrantLock();

    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print5(){
        lock.lock();
        try {
            // 1、判断
            while (number != 1){
                c1.await();           // A等待
            }
            // 2、干活
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            // 3、通知
            number = 2;       //修改标志位
            c2.signal();      //只通知2号线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10(){
        lock.lock();
        try {
            // 1、判断
            while (number != 2){
                c2.await();           // B等待
            }
            // 2、干活
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            // 3、通知
            number = 3;       //修改标志位
            c3.signal();      //只通知3号线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15(){
        lock.lock();
        try {
            // 1、判断
            while (number != 3){
                c3.await();           // A等待
            }
            // 2、干活
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            // 3、通知
            number = 1;       //修改标志位
            c1.signal();      //只通知1号线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}