package com.zhaoy.java;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1 验证volatile的可见性
 *     1.1 假如int number = 0; number变量之前根本没有添加volatile关键字修饰 ;没有可见性
 *     1.2 添加了volatile, 可以解决可见性问题
 *
 * 2 验证volatile不保证原子性
 *     2.1 原子性是啥意思
 *     2.2 volatile不保证原子性的案例演示
 *     2.3 why?
 *     2.4 如何解决原子性？
 *          加synchronized
 */
public class VolatileDemo {
    public static void main(String[] args) {   //main是一切方法的运行入口
        MyData myData = new MyData();

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000 ; j++) {
                    myData.addPlusPlus();
                    myData.addMyAtomic();
                }
            },String.valueOf(i)).start();
        }

        // 需要等待上面20个线程全部计算完成后，再用main线程取得最终的结果值看是多少？
        // 这步很重要
        while (Thread.activeCount() > 2){   // 默认main和gc线程
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName()+"\t int type,finally number value: "+myData.number);
        System.out.println(Thread.currentThread().getName()+"\t AtomicInteger type,finally number value: "+myData.atomicInteger);


    }

    //  Volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改。
    public static void setOkByVolatile() {
        MyData myData = new MyData();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t come in");
            try{
                TimeUnit.SECONDS.sleep(3);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            myData.addTO60();
            System.out.println(Thread.currentThread().getName()+"\t update number value: "+myData.number);
        },"AAA").start();

        //第二个线程就是我们的main线程
        while (myData.number == 0){
            // main线程就一直在这里等待循环，直到number值不再等于零
        }
        System.out.println(Thread.currentThread().getName()+"\t mission is over,main get number value: "+myData.number);
    }

}

class MyData{
    volatile int number = 0;

    public void addTO60(){
        this.number = 60;
    }

    // 请注意，此时的number前面是加了volatile关键字修饰的，volatile不保证原子性
    public void addPlusPlus(){
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();

    public void addMyAtomic(){
        atomicInteger.getAndIncrement();
    }
}
