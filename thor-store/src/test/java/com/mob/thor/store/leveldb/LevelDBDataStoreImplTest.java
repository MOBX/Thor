/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.leveldb;

import java.io.Serializable;

import com.lamfire.json.JSON;
import com.mob.thor.store.api.ThorStore;
import com.mob.thor.store.instance.leveldb.LevelDBDataStoreImpl;

/**
 * @author zxc Dec 28, 2015 6:08:18 PM
 */
@SuppressWarnings("rawtypes")
public class LevelDBDataStoreImplTest {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ThorStore thorStore = new LevelDBDataStoreImpl<User>("mytest");
        thorStore.put(new User("hello world!"));

        Thread.sleep(1000);

        System.out.println(thorStore.get());
    }

    public static class User implements Serializable {

        private static final long serialVersionUID = 8247044944924579817L;

        private String            name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
