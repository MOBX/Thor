/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Thor项目使用的ToStringStyle
 * 
 * <pre>
 * 默认Style输出格式：
 * Person[name=John Doe,age=33,smoker=false ,time=2015-12-01 00:00:00]
 * </pre>
 * 
 * @author zxc Dec 24, 2015 4:46:08 PM
 */
public class ThorToStringStyle extends ToStringStyle {

    private static final long         serialVersionUID = 3712495873025727360L;

    private static final String       DEFAULT_TIME     = "yyyy-MM-dd HH:mm:ss";
    private static final String       DEFAULT_DAY      = "yyyy-MM-dd";

    /**
     * <pre>
     * 输出格式：
     * Person[name=John Doe,age=33,smoker=false ,time=2010-04-01 00:00:00]
     * </pre>
     */
    public static final ToStringStyle TIME_STYLE       = new ThorDateStyle(DEFAULT_TIME);

    /**
     * <pre>
     * 输出格式：
     * Person[name=John Doe,age=33,smoker=false ,day=2010-04-01]
     * </pre>
     */
    public static final ToStringStyle DAY_STYLE        = new ThorDateStyle(DEFAULT_DAY);

    /**
     * <pre>
     * 输出格式：
     * Person[name=John Doe,age=33,smoker=false ,time=2010-04-01 00:00:00]
     * </pre>
     */
    public static final ToStringStyle DEFAULT_STYLE    = ThorToStringStyle.TIME_STYLE;

    private static class ThorDateStyle extends ToStringStyle {

        private static final long serialVersionUID = 5208917932254652886L;

        private String            pattern;

        public ThorDateStyle(String pattern) {
            super();
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            this.pattern = pattern;
        }

        protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value instanceof Date) {
                value = new SimpleDateFormat(pattern).format(value);
            }
            buffer.append(value);
        }
    }
}
