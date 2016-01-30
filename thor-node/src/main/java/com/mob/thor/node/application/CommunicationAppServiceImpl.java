/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.common.utils.StringUtils;
import com.mob.thor.communication.core.model.Event;
import com.mob.thor.node.application.convert.EventConvert;
import com.mob.thor.node.application.event.DataEvent;
import com.mob.thor.node.application.exception.NodeHandleException;
import com.mob.thor.store.api.ThorStore;
import com.mob.thor.store.instance.leveldb.LevelDBDataStoreImpl;

/**
 * @author zxc Dec 24, 2015 5:55:40 PM
 */
public class CommunicationAppServiceImpl implements CommunicationAppService {

    private static final Logger logger = LoggerFactory.getLogger(CommunicationAppServiceImpl.class);

    private ExecutorService     threadPool;
    private Runnable            exec;

    public CommunicationAppServiceImpl() {

    }

    public CommunicationAppServiceImpl(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public CommunicationAppServiceImpl(ExecutorService threadPool, Runnable exec) {
        this.threadPool = threadPool;
        this.exec = exec;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public Event handleEvent(Event event) {
        throw new IllegalArgumentException();
    }

    public Runnable getExec() {
        return exec;
    }

    public void setExec(Runnable exec) {
        this.exec = exec;
    }

    @Override
    public boolean onSend(final DataEvent event) {
        if (event == null) {
            logger.error("event is null");
            return false;
        }
        logger.error("[type:{},event:{}]", event.getType(), event);
        Object obj = event.getObject();
        if (obj == null) {
            return false;
        }
        if (exec == null) {
            throw new NodeHandleException("exec is null");
        }
        if (threadPool == null) {
            throw new NodeHandleException("threadPool is null");
        }
        if (obj instanceof String) {
            EventConvert.fixdSimpleObject(exec, event, obj);
        } else {
            EventConvert.fixdComplexObject(exec, obj);
        }
        threadPool.submit(exec);
        return true;
    }

    @Override
    public boolean onPush(DataEvent event) {
        if (event == null) {
            logger.error("event is null");
            return false;
        }
        logger.info("[^_^ receive ^_^ type:{},event:{}]", event.getType(), event);
        Object obj = event.getObject();
        String topic = event.getTopic();
        if (obj == null || StringUtils.isEmpty(topic)) {
            return false;
        }
        ThorStore<Serializable> thorStore = new LevelDBDataStoreImpl<Serializable>(topic);
        try {
            thorStore.put((Serializable) obj);
            return true;
        } catch (Exception e) {
            logger.error("Serializable error! [topic:{},event:{}]", topic, event);
        }
        return false;
    }

    @Override
    public Object onPull(DataEvent event) {
        if (event == null) {
            logger.error("event is null");
            return null;
        }
        logger.info("[type:{},event:{}]", event.getType(), event);
        String topic = event.getTopic();
        if (StringUtils.isEmpty(topic)) {
            return null;
        }
        ThorStore<Serializable> thorStore = new LevelDBDataStoreImpl<Serializable>(topic);
        try {
            return thorStore.get();
        } catch (Exception e) {
            logger.error("Serializable error! [topic:{},event:{}]", topic, event);
        }
        return null;
    }

    @Override
    public boolean onSubscribe(DataEvent event) {
        return false;
    }
}
