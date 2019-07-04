package com.zhaoy.java;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  生产者消费者模式
 *  阻塞队列版  3.0版
 */

 // 资源类
class MyRescource{
    private volatile boolean FLAG = true;  //默认开启，进行生产+消费   //高并发可见性     //标志位

    private AtomicInteger atomicInteger = new AtomicInteger();              //生产一个消费一个

    BlockingQueue<String> blockingQueue = null;

    public MyRescource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd() throws Exception{
        String data = null;
        boolean retValue;
        while (FLAG){
            data = atomicInteger.incrementAndGet()+"";
            retValue = blockingQueue.offer(data,2L, TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName()+"\t 插入队列"+data+"成功");
            }else{
                System.out.println(Thread.currentThread().getName()+"\t 插入队列"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t大老板叫停了，表示FLAG=false，生产动作结束");
    }

    public void myConsumer() throws Exception{
        String result = null;
        while (FLAG){
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if(null == result || result.equalsIgnoreCase("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName()+"\t 超过2秒钟没有取到蛋糕，消费退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName()+"\t 消费队列蛋糕"+result+"成功");
        }

    }

    public void stop() throws Exception{
        this.FLAG = false;

    }
}

/**
 * volatile/CAS/atomicInteger/BlockQueue/线程交互/原子引用
 */
public class ProdConsumer_BlockQueueDemo {

    public static void main(String[] args) {
        MyRescource myRescource = new MyRescource(new ArrayBlockingQueue<>(10));      // 构造注入歘接口

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 生产线程启动");
            try {
                myRescource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } , "Prod").start();


        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 消费线程启动");
            System.out.println();
            System.out.println();
            try {
                myRescource.myConsumer();
                System.out.println();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } , "Consumer").start();

        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("7秒钟时间到，大老板main线程叫停，活动结束");

        try {
            myRescource.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
