package com.isyscore.os.driver.utils;

public class TimerHandle {
    public long interval;
    public long timeout;
    public Run run;
    public interface Run {
        void operation(TimerHandle timerHandle);
    }
    public TimerHandle(Run run) {
        this.run = run;
    }
}