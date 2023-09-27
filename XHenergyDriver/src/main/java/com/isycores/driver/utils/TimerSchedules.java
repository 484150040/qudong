package com.isycores.driver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimerSchedules {
    volatile private Lock lock = new ReentrantLock();
    private Thread timeTask;
    private boolean timeTaskFlag;
    private final long steptime;
    private Set<TimerHandle> timerHandles = new HashSet<>();
    public TimerSchedules(long steptime) {
        this.steptime = steptime;
    }
    private static final Logger log = LoggerFactory.getLogger(TimerSchedules.class);
    public void start() {
        timeTaskFlag = true;
        timeTask = new Thread(()->{
            while (timeTaskFlag) {
                try {
                    Thread.sleep(steptime);
                    lock.lock();
                    try {
                        for (TimerHandle timerHandle : timerHandles) {
                            timerHandle.timeout = timerHandle.timeout - steptime;
                            if (timerHandle.timeout <= 0) {
                                lock.unlock();
                                timerHandle.run.operation(timerHandle);
                                lock.lock();
                                if (timerHandle.interval == 0) {
                                    timerHandles.remove(timerHandle);
                                    break;
                                } else {
                                    timerHandle.timeout = timerHandle.interval;
                                }
                            }
                        }
                    } catch (ConcurrentModificationException e) {
                        log.error(e.getMessage(),e);
                    }
                    lock.unlock();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeTask.start();
    }

    public void stop() {
        timeTaskFlag = false;
        try {
            timeTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void register(TimerHandle timerHandle,long timeout,long interval) {
        lock.lock();
        timerHandle.timeout = timeout;
        timerHandle.interval = interval;
        timerHandles.add(timerHandle);
        lock.unlock();
    }

    public void unregister(TimerHandle timerHandle) {
        lock.lock();
        try {
            timerHandles.remove(timerHandle);
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
        lock.unlock();
    }

    public static void main(String[] args) {
        TimerSchedules timer = new TimerSchedules(1000);
        TimerHandle timerHandle1 = new TimerHandle((handle)->{
            System.out.println("timerHandle1");
        });

        TimerHandle timerHandle2 = new TimerHandle((handle)->{
            System.out.println("timerHandle2");
        });

        TimerHandle timerHandle3 = new TimerHandle((handle)->{
            System.out.println("timerHandle3");
        });

        TimerHandle timerHandle4 = new TimerHandle((handle)->{
            System.out.println("timerHandle4");
        });

        timer.start();

        timer.register(timerHandle1,0,0);
        timer.register(timerHandle4,5000,1000);
        timer.register(timerHandle3,1000,0);

        for (int i = 0; i < 1000;i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.register(timerHandle2,0,2000);
            timer.unregister(timerHandle3);
        }


        timer.stop();
    }
}
