/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common.extension;

/**
 * ExtensionFactory
 * 
 * @author william.liangf
 * @export
 */
@SPI
public interface ExtensionFactory {

    /**
     * Get extension.
     * 
     * @param type object type.
     * @param name object name.
     * @return object instance.
     */
    <T> T getExtension(Class<T> type, String name);
}
