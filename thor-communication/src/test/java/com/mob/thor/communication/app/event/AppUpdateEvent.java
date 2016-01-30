/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.app.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.mob.thor.communication.core.model.Event;

/**
 * @author zxc Dec 24, 2015 5:55:22 PM
 */
public class AppUpdateEvent extends Event {

    private static final long serialVersionUID = 4643955092115538570L;

    public AppUpdateEvent(){
        super(AppEventType.update);
    }

    private String     name;
    private BigInteger bigIntegerValue;
    private BigDecimal bigDecimalValue;
    private UpdateData data;

    public static class UpdateData implements Serializable {

        private static final long serialVersionUID = -2591770066519646446L;
        private String            name;
        private BigInteger        bigIntegerValue;
        private BigDecimal        bigDecimalValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigInteger getBigIntegerValue() {
            return bigIntegerValue;
        }

        public void setBigIntegerValue(BigInteger bigIntegerValue) {
            this.bigIntegerValue = bigIntegerValue;
        }

        public BigDecimal getBigDecimalValue() {
            return bigDecimalValue;
        }

        public void setBigDecimalValue(BigDecimal bigDecimalValue) {
            this.bigDecimalValue = bigDecimalValue;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getBigIntegerValue() {
        return bigIntegerValue;
    }

    public void setBigIntegerValue(BigInteger bigIntegerValue) {
        this.bigIntegerValue = bigIntegerValue;
    }

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public UpdateData getData() {
        return data;
    }

    public void setData(UpdateData data) {
        this.data = data;
    }
}
