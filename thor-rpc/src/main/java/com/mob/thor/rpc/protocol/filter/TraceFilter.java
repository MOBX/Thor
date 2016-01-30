package com.mob.thor.rpc.protocol.filter;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.json.JSON;
import com.mob.thor.rpc.common.logger.Logger;
import com.mob.thor.rpc.common.logger.LoggerFactory;
import com.mob.thor.rpc.common.utils.ConcurrentHashSet;
import com.mob.thor.rpc.remoting.api.ThorChannel;

@Activate(group = Constants.PROVIDER)
public class TraceFilter implements Filter {

    private static final Logger                                  logger      = LoggerFactory.getLogger(TraceFilter.class);

    private static final String                                  TRACE_MAX   = "trace.max";

    private static final String                                  TRACE_COUNT = "trace.count";

    private static final ConcurrentMap<String, Set<ThorChannel>> tracers     = new ConcurrentHashMap<String, Set<ThorChannel>>();

    public static void addTracer(Class<?> type, String method, ThorChannel channel, int max) {
        channel.setAttribute(TRACE_MAX, max);
        channel.setAttribute(TRACE_COUNT, new AtomicInteger());
        String key = method != null && method.length() > 0 ? type.getName() + "." + method : type.getName();
        Set<ThorChannel> channels = tracers.get(key);
        if (channels == null) {
            tracers.putIfAbsent(key, new ConcurrentHashSet<ThorChannel>());
            channels = tracers.get(key);
        }
        channels.add(channel);
    }

    public static void removeTracer(Class<?> type, String method, ThorChannel channel) {
        channel.removeAttribute(TRACE_MAX);
        channel.removeAttribute(TRACE_COUNT);
        String key = method != null && method.length() > 0 ? type.getName() + "." + method : type.getName();
        Set<ThorChannel> channels = tracers.get(key);
        if (channels != null) {
            channels.remove(channel);
        }
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long end = System.currentTimeMillis();
        if (tracers.size() > 0) {
            String key = invoker.getInterface().getName() + "." + invocation.getMethodName();
            Set<ThorChannel> channels = tracers.get(key);
            if (channels == null || channels.size() == 0) {
                key = invoker.getInterface().getName();
                channels = tracers.get(key);
            }
            if (channels != null && channels.size() > 0) {
                for (ThorChannel channel : new ArrayList<ThorChannel>(channels)) {
                    if (channel.isConnected()) {
                        try {
                            int max = 1;
                            Integer m = (Integer) channel.getAttribute(TRACE_MAX);
                            if (m != null) {
                                max = (int) m;
                            }
                            int count = 0;
                            AtomicInteger c = (AtomicInteger) channel.getAttribute(TRACE_COUNT);
                            if (c == null) {
                                c = new AtomicInteger();
                                channel.setAttribute(TRACE_COUNT, c);
                            }
                            count = c.getAndIncrement();
                            if (count < max) {
                                String prompt = channel.getUrl().getParameter(Constants.PROMPT_KEY,
                                                                              Constants.DEFAULT_PROMPT);
                                channel.send("\r\n" + RpcContext.getContext().getRemoteAddress() + " -> "
                                             + invoker.getInterface().getName() + "." + invocation.getMethodName()
                                             + "(" + JSON.json(invocation.getArguments()) + ")" + " -> "
                                             + JSON.json(result.getValue()) + "\r\nelapsed: " + (end - start) + " ms."
                                             + "\r\n\r\n" + prompt);
                            }
                            if (count >= max - 1) {
                                channels.remove(channel);
                            }
                        } catch (Throwable e) {
                            channels.remove(channel);
                            logger.warn(e.getMessage(), e);
                        }
                    } else {
                        channels.remove(channel);
                    }
                }
            }
        }
        return result;
    }
}
