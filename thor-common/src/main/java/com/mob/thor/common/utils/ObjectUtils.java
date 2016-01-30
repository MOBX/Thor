/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zxc Apr 12, 2013 1:23:30 PM
 */
public class ObjectUtils extends org.apache.commons.lang.ObjectUtils {

    private static Logger log = LoggerFactory.getLogger(ObjectUtils.class);

    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    public static boolean containsElement(Object[] array, Object element) {
        if (array == null) {
            return false;
        }
        for (Object arrayEle : array) {
            if (nullSafeEquals(arrayEle, element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
        return containsConstant(enumValues, constant, false);
    }

    public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
        for (Enum<?> candidate : enumValues) {
            if (caseSensitive ? candidate.toString().equals(constant) : candidate.toString().equalsIgnoreCase(constant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 把对象中的string数据类型进行一次trim操作
     * 
     * @param object
     * @throws Exception
     */
    public static void trim(Object object) {
        if (object == null) {
            return;
        }
        try {
            trimStringField(object, object.getClass());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 把对象中的string数据类型进行一次trim操作
     * 
     * @param object
     * @param parent 所有的父类是否需要trim
     * @throws Exception
     */
    public static void trim(Object object, boolean parentClass) {
        if (object == null) {
            return;
        }
        if (parentClass) {
            trim(object, null);
        } else {
            trim(object);
        }
    }

    /**
     * @param obj
     */
    public static void trim(Object obj, Class<?> stopClass) {
        if (obj == null) {
            return;
        }
        if (stopClass == null) {
            stopClass = Object.class;
        }
        Class<?> objClass = obj.getClass();
        boolean nextBreak = false;
        while (true) {
            try {
                trimStringField(obj, objClass);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                break;
            }
            if (nextBreak) {
                break;
            }
            objClass = objClass.getSuperclass();
            if (objClass == null || objClass == Object.class) {
                break;
            }
            if (objClass == stopClass) {
                nextBreak = true;
            }
        }
    }

    /**
     * 把对象中的string数据类型进行一次trim操作
     * 
     * @param object
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void trimStringField(Object object, Class<?> clazz) throws Exception {
        if (object instanceof Map<?, ?>) {
            Map<Object, Object> target = new HashMap<Object, Object>();
            for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();

                if (key instanceof String) {
                    key = StringUtils.trim((String) key);
                } else {
                    trim(key);
                }

                if (value instanceof String) {
                    value = StringUtils.trim((String) value);
                    value = StringUtils.replace((String) value, "\"", StringUtils.EMPTY);
                } else {
                    trim(value);
                }
                target.put(key, value);
            }
            ((Map<?, ?>) object).clear();
            ((Map) object).putAll((Map) target);
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                boolean isFoolback = false;
                if (field.isAccessible() == false) {
                    isFoolback = true;
                    field.setAccessible(true);
                }
                String value = (String) field.get(object);
                if (StringUtils.isNotEmpty(value)) {
                    value = value.trim();
                    field.set(object, value);
                }
                if (isFoolback) {
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * 注解到对象复制，只复制能匹配上的方法。
     * 
     * @param annotation
     * @param object
     */
    public static void annotationToObject(Object annotation, Object object) {
        if (annotation != null) {
            Class<?> annotationClass = annotation.getClass();
            Class<?> objectClass = object.getClass();
            for (Method m : objectClass.getMethods()) {
                if (StringUtils.startsWith(m.getName(), "set")) {
                    try {
                        String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                        Object obj = annotationClass.getMethod(s).invoke(annotation);
                        if (obj != null && !"".equals(obj.toString())) {
                            if (object == null) {
                                object = objectClass.newInstance();
                            }
                            m.invoke(object, obj);
                        }
                    } catch (Exception e) {
                        // 忽略所有设置失败方法
                    }
                }
            }
        }
    }

    /**
     * 序列化对象
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (object != null) {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     * 
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            if (bytes != null && bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            if (o1 instanceof Object[] && o2 instanceof Object[]) {
                return Arrays.equals((Object[]) o1, (Object[]) o2);
            }
            if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
                return Arrays.equals((boolean[]) o1, (boolean[]) o2);
            }
            if (o1 instanceof byte[] && o2 instanceof byte[]) {
                return Arrays.equals((byte[]) o1, (byte[]) o2);
            }
            if (o1 instanceof char[] && o2 instanceof char[]) {
                return Arrays.equals((char[]) o1, (char[]) o2);
            }
            if (o1 instanceof double[] && o2 instanceof double[]) {
                return Arrays.equals((double[]) o1, (double[]) o2);
            }
            if (o1 instanceof float[] && o2 instanceof float[]) {
                return Arrays.equals((float[]) o1, (float[]) o2);
            }
            if (o1 instanceof int[] && o2 instanceof int[]) {
                return Arrays.equals((int[]) o1, (int[]) o2);
            }
            if (o1 instanceof long[] && o2 instanceof long[]) {
                return Arrays.equals((long[]) o1, (long[]) o2);
            }
            if (o1 instanceof short[] && o2 instanceof short[]) {
                return Arrays.equals((short[]) o1, (short[]) o2);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Map<String, String> kvMap = new HashMap<String, String>();
        kvMap.put(" _zll_ ", " zsdfasdaaaaaaaaaaa ");
        kvMap.put("                               zxc ", " zsdfasdaaqrwerqweraaaaa ");
        kvMap.put(" _qwl", " rrrrrrrrrrrrrrrrrrrr  ");
        kvMap.put("12safa", "qweweq");
        trim(kvMap);
        System.out.println(kvMap);
    }
}
