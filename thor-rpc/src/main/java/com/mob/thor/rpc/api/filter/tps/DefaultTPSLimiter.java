package com.mob.thor.rpc.api.filter.tps;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mob.thor.rpc.api.Invocation;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;

public class DefaultTPSLimiter implements TPSLimiter {

    private final ConcurrentMap<String, StatItem> stats = new ConcurrentHashMap<String, StatItem>();

    public boolean isAllowable(URL url, Invocation invocation) {
        int rate = url.getParameter(Constants.TPS_LIMIT_RATE_KEY, -1);
        long interval = url.getParameter(Constants.TPS_LIMIT_INTERVAL_KEY, Constants.DEFAULT_TPS_LIMIT_INTERVAL);
        String serviceKey = url.getServiceKey();
        if (rate > 0) {
            StatItem statItem = stats.get(serviceKey);
            if (statItem == null) {
                stats.putIfAbsent(serviceKey, new StatItem(serviceKey, rate, interval));
                statItem = stats.get(serviceKey);
            }
            return statItem.isAllowable(url, invocation);
        } else {
            StatItem statItem = stats.get(serviceKey);
            if (statItem != null) {
                stats.remove(serviceKey);
            }
        }
        return true;
    }
}
