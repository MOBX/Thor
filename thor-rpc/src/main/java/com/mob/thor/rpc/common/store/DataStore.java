package com.mob.thor.rpc.common.store;

import java.util.Map;

import com.mob.thor.rpc.common.extension.SPI;

@SPI("simple")
public interface DataStore {

    /**
     * return a snapshot value of componentName
     */
    Map<String, Object> get(String componentName);

    Object get(String componentName, String key);

    void put(String componentName, String key, Object value);

    void remove(String componentName, String key);
}
