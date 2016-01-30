package com.mob.thor.rpc.api;

import java.util.Map;

/**
 * Invocation. (API, Prototype, NonThreadSafe)
 * 
 * @serial Don't change the class name and package name.
 * @see com.mob.thor.rpc.api.Invoker#invoke(Invocation)
 * @see com.mob.thor.rpc.api.RpcInvocation
 * @author zxc
 */
public interface Invocation {

    /**
     * get method name.
     * 
     * @serial
     * @return method name.
     */
    String getMethodName();

    /**
     * get parameter types.
     * 
     * @serial
     * @return parameter types.
     */
    Class<?>[] getParameterTypes();

    /**
     * get arguments.
     * 
     * @serial
     * @return arguments.
     */
    Object[] getArguments();

    /**
     * get attachments.
     * 
     * @serial
     * @return attachments.
     */
    Map<String, String> getAttachments();

    /**
     * get attachment by key.
     * 
     * @serial
     * @return attachment value.
     */
    String getAttachment(String key);

    /**
     * get attachment by key with default value.
     * 
     * @serial
     * @return attachment value.
     */
    String getAttachment(String key, String defaultValue);

    /**
     * get the invoker in current context.
     * 
     * @transient
     * @return invoker.
     */
    Invoker<?> getInvoker();

}
