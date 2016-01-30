/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mob.thor.common.utils.Reflections;
import com.mob.thor.common.utils.StringUtils;
import com.mob.thor.communication.core.CommunicationEndpoint;
import com.mob.thor.communication.core.exception.CommunicationException;
import com.mob.thor.communication.core.model.Event;
import com.mob.thor.communication.core.model.HeartEvent;
import com.mob.thor.communication.core.registry.CommunicationRegistry;

/**
 * 默认的endpoint实现
 * 
 * @author zxc Dec 24, 2015 4:41:14 PM
 */
public abstract class AbstractCommunicationEndpoint implements CommunicationEndpoint {

    // 需要禁止输出详细内容的事件
    private static final Logger logger         = LoggerFactory.getLogger(CommunicationEndpoint.class);

    private static final String DEFAULT_METHOD = "handleEvent";

    /**
     * 处理指定的事件
     */
    public Object acceptEvent(Event event) {
        if (event instanceof HeartEvent) {
            return event;
        }
        try {
            Object action = CommunicationRegistry.getAction(event.getType());
            if (action != null) {
                String methodName = "on" + StringUtils.capitalize(event.getType().toString());
                Method method = Reflections.findMethod(action.getClass(), methodName, new Class[] { event.getClass() });
                if (method == null) {
                    methodName = DEFAULT_METHOD;
                    method = Reflections.findMethod(action.getClass(), methodName, new Class[] { event.getClass() });
                    if (method == null) {
                        method = Reflections.findMethod(action.getClass(), methodName, new Class[] { Event.class });
                    }
                }
                if (method != null) {
                    try {
                        Reflections.makeAccessible(method);
                        return method.invoke(action, new Object[] { event });
                    } catch (Throwable e) {
                        throw new CommunicationException("method_invoke_error:" + methodName, e);
                    }
                } else {
                    throw new CommunicationException("no_method_error for[" + StringUtils.capitalize(event.getType().toString())+ "] in Class[" + action.getClass().getName() + "]");
                }
            }
            throw new CommunicationException("eventType_no_action", event.getType().name());
        } catch (RuntimeException e) {
            logger.error("endpoint_error", e);
            throw e;
        } catch (Exception e) {
            logger.error("endpoint_error", e);
            throw new CommunicationException(e);
        }
    }
}
