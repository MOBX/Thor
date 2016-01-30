/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.node;

import java.io.Serializable;
import java.util.Date;

import com.lamfire.json.JSON;

/**
 * @author zxc Dec 31, 2015 4:35:30 PM
 */
public class HelloWorld implements Serializable {

    private static final long serialVersionUID = 5196188375530365632L;

    private String            name;
    private Long              create           = new Date().getTime();

    public HelloWorld(String name) {
        this.name = name;
    }

    public HelloWorld(String name, Long create) {
        this.name = name;
        this.create = create;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreate() {
        return create;
    }

    public void setCreate(Long create) {
        this.create = create;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
