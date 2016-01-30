package com.mob.thor.rpc.api.filter.tps;

import java.util.concurrent.atomic.AtomicInteger;

import com.mob.thor.rpc.api.Invocation;
import com.mob.thor.rpc.common.URL;

class StatItem {

    private String        name;

    private long          lastResetTime;

    private long          interval;

    private AtomicInteger token;

    private int           rate;

    StatItem(String name, int rate, long interval) {
        this.name = name;
        this.rate = rate;
        this.interval = interval;
        this.lastResetTime = System.currentTimeMillis();
        this.token = new AtomicInteger(rate);
    }

    public boolean isAllowable(URL url, Invocation invocation) {
        long now = System.currentTimeMillis();
        if (now > lastResetTime + interval) {
            token.set(rate);
            lastResetTime = now;
        }

        int value = token.get();
        boolean flag = false;
        while (value > 0 && !flag) {
            flag = token.compareAndSet(value, value - 1);
            value = token.get();
        }

        return flag;
    }

    long getLastResetTime() {
        return lastResetTime;
    }

    int getToken() {
        return token.get();
    }

    public String toString() {
        return new StringBuilder(32).append("StatItem ").append("[name=").append(name).append(", ").append("rate = ").append(rate).append(", ").append("interval = ").append(interval).append("]").toString();
    }

}
