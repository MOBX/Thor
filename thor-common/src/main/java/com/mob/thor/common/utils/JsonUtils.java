/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.utils;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.*;

/**
 * @author zxc Dec 1, 2015 5:35:57 PM
 */
@SuppressWarnings("unchecked")
public class JsonUtils {

    public static <T> T unmarshalFromByte(byte[] bytes, Class<T> targetClass) {
        return (T) JSON.parseObject(bytes, targetClass);// 默认为UTF-8
    }

    public static <T> T unmarshalFromByte(byte[] bytes, TypeReference<T> type) {
        return (T) JSON.parseObject(bytes, type.getType());
    }

    public static byte[] marshalToByte(Object obj) {
        return JSON.toJSONBytes(obj); // 默认为UTF-8
    }

    public static byte[] marshalToByte(Object obj, SerializerFeature... features) {
        return JSON.toJSONBytes(obj, features); // 默认为UTF-8
    }

    public static <T> T unmarshalFromString(String json, Class<T> targetClass) {
        return (T) JSON.parseObject(json, targetClass);// 默认为UTF-8
    }

    public static <T> T unmarshalFromString(String json, TypeReference<T> type) {
        return (T) JSON.parseObject(json, type);// 默认为UTF-8
    }

    public static String marshalToString(Object obj) {
        return JSON.toJSONString(obj); // 默认为UTF-8
    }

    public static String marshalToString(Object obj, SerializerFeature... features) {
        return JSON.toJSONString(obj, features); // 默认为UTF-8
    }

    /**
     * 可以允许指定一些过滤字段进行生成json对象
     */
    public static String marshalToString(Object obj, String... fliterFields) {
        final List<String> propertyFliters = Arrays.asList(fliterFields);
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.getPropertyFilters().add(new PropertyFilter() {

                public boolean apply(Object source, String name, Object value) {
                    return !propertyFliters.contains(name);
                }

            });
            serializer.write(obj);
            return out.toString();
        } finally {
            out.close();
        }
    }
}
