package com.mob.thor.rpc.api;

import java.util.Map;

/**
 * RPC invoke result. (API, Prototype, NonThreadSafe)
 * 
 * @serial Don't change the class name and package name.
 * @see com.mob.thor.rpc.api.Invoker#invoke(Invocation)
 * @see com.mob.thor.rpc.api.RpcResult
 * @author zxc
 */
public interface Result {

    /**
     * Get invoke result.
     * 
     * @return result. if no result return null.
     */
    Object getValue();

    /**
     * Get exception.
     * 
     * @return exception. if no exception return null.
     */
    Throwable getException();

    /**
     * Has exception.
     * 
     * @return has exception.
     */
    boolean hasException();

    /**
     * Recreate. <code>
     * if (hasException()) {
     *     throw getException();
     * } else {
     *     return getValue();
     * }
     * </code>
     * 
     * @return result.
     * @throws if has exception throw it.
     */
    Object recreate() throws Throwable;

    /**
     * @deprecated Replace to getValue()
     * @see com.mob.thor.rpc.Result#getValue()
     */
    @Deprecated
    Object getResult();

    /**
     * get attachments.
     *
     * @return attachments.
     */
    Map<String, String> getAttachments();

    /**
     * get attachment by key.
     *
     * @return attachment value.
     */
    String getAttachment(String key);

    /**
     * get attachment by key with default value.
     *
     * @return attachment value.
     */
    String getAttachment(String key, String defaultValue);

}
