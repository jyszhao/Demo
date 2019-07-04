package com.zhaoy.java;


import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"\t国，被灭");
                countDownLatch.countDown();
            },CountryEnum.forEach_CountryEnum(i).getRetMessage()).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"\t *************秦帝国，一统华夏");

        System.out.println(CountryEnum.ONE);


    }


    public static void closeDoor() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"\t上完自习，离开教室");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"\t *************班长最后关门走人");
    }


    public enum CountryEnum{            // 枚举可以想象成一个个数据库的表
        ONE(1,"齐"),TWO(2,"楚"),THREE(3,"燕"),
        FOUR(4,"赵"),FIVE(5,"魏"),SIX(6,"韩");

        CountryEnum(Integer retCode, String retMessage) {
            this.retCode = retCode;
            this.retMessage = retMessage;
        }

        public Integer getRetCode() {
            return retCode;
        }

        public String getRetMessage() {
            return retMessage;
        }

        private Integer retCode;

        private String retMessage;

        public static CountryEnum forEach_CountryEnum(int index){

            CountryEnum[] myArray = CountryEnum.values();
            for (CountryEnum element : myArray) {
                if(index == element.getRetCode()){
                    return element;
                }
            }
            return null;
        }

    }
}
