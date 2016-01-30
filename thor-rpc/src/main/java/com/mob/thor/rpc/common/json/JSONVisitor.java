package com.mob.thor.rpc.common.json;

public interface JSONVisitor {

    public static final String CLASS_PROPERTY = "class";

    /**
     * parse begin .
     */
    void begin();

    /**
     * parse end.
     * 
     * @param obj root obj.
     * @param isValue is json value.
     * @return parse result.
     * @throws ParseException
     */
    Object end(Object obj, boolean isValue) throws ParseException;

    /**
     * object begin.
     * 
     * @throws ParseException
     */
    void objectBegin() throws ParseException;

    /**
     * object end, return object value.
     * 
     * @param count property count.
     * @return object value.
     * @throws ParseException
     */
    Object objectEnd(int count) throws ParseException;

    /**
     * object property name.
     * 
     * @param name name.
     * @throws ParseException
     */
    void objectItem(String name) throws ParseException;

    /**
     * object property value.
     * 
     * @param obj obj.
     * @param isValue is json value.
     * @throws ParseException
     */
    void objectItemValue(Object obj, boolean isValue) throws ParseException;

    /**
     * array begin.
     * 
     * @throws ParseException
     */
    void arrayBegin() throws ParseException;

    /**
     * array end, return array value.
     * 
     * @param count count.
     * @return array value.
     * @throws ParseException
     */
    Object arrayEnd(int count) throws ParseException;

    /**
     * array item.
     * 
     * @param index index.
     * @throws ParseException
     */
    void arrayItem(int index) throws ParseException;

    /**
     * array item.
     * 
     * @param index index.
     * @param obj item.
     * @param isValue is json value.
     * @throws ParseException
     */
    void arrayItemValue(int index, Object obj, boolean isValue) throws ParseException;
}
