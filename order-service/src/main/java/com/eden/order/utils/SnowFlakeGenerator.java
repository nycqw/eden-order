package com.eden.order.utils;


public class SnowFlakeGenerator {

    private static volatile SnowFlake snowFlake;

    private static final Object lock = new Object();

    public static long nextId() {
        init();
        return snowFlake.nextId();
    }

    private static void init() {
        if (snowFlake == null) {
            synchronized (lock) {
                if (snowFlake == null) {
                    String machineId = System.getProperty("machineId", "1");
                    String dataCenterId = System.getProperty("dataCenterId", "1");
                    snowFlake = new SnowFlake(Integer.valueOf(machineId), Integer.valueOf(dataCenterId));
                }
            }
        }
    }

}
