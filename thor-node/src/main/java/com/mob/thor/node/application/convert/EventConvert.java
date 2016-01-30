/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.node.application.convert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.mob.thor.common.utils.Reflections;
import com.mob.thor.common.utils.StringUtils;
import com.mob.thor.node.application.event.DataEvent;
import com.mob.thor.node.application.exception.NodeHandleException;

/**
 * @author zxc Dec 31, 2015 5:25:14 PM
 */
public class EventConvert {

    public static void fixdSimpleObject(Runnable exec, DataEvent event, Object obj) {
        List<Method> methodList = Reflections.listMethod(exec.getClass(), new Class[] { event.getClass() });
        for (Method method : methodList) {
            if (StringUtils.startsWith(method.getName(), "set")
                && Arrays.equals(new Class[] { obj.getClass() }, method.getParameterTypes())) {
                try {
                    Reflections.makeAccessible(method);
                    method.invoke(exec, new Object[] { obj });
                } catch (Throwable e) {
                    throw new NodeHandleException("method_invoke_error:String", e);
                }
            }
        }
    }

    public static void fixdComplexObject(Runnable exec, Object obj) {
        Class<?> clazz = obj.getClass();
        String methodName = "set" + StringUtils.capitalize(clazz.getSimpleName());
        Method method = Reflections.findMethod(exec.getClass(), methodName, new Class[] { clazz });
        if (method != null) {
            try {
                Reflections.makeAccessible(method);
                method.invoke(exec, new Object[] { obj });
            } catch (Throwable e) {
                throw new NodeHandleException("method_invoke_error:" + methodName, e);
            }
        }
    }
}
