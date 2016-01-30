/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.store.instance.leveldb;

import java.util.Properties;

import com.lamfire.pandora.*;
import com.lamfire.utils.PropertiesUtils;

/**
 * @author zxc Dec 28, 2015 5:21:25 PM
 */
public class LevelDBHelper {

    private final Pandora           pandora;
    private static final Properties pro = PropertiesUtils.load("level-db-data.properties", LevelDBHelper.class);
    private static String           storeDir;
    private static String           name;

    static {
        storeDir = pro.getProperty("store.dir");
        name = pro.getProperty("data.name");
    }

    private LevelDBHelper() {
        PandoraMaker maker = new PandoraMaker(storeDir, name);
        maker.createIfMissing(true);
        pandora = maker.make();
    }

    private static class SingletonClassInstance {

        private static final LevelDBHelper instance = new LevelDBHelper();
    }

    public static LevelDBHelper getInstance() {
        return SingletonClassInstance.instance;
    }

    public FireIncrement getIncrement(String name) {
        return pandora.getFireIncrement(name);
    }

    public FireQueue getQueue(String name) {
        return pandora.getFireQueue(name);
    }

    public FireMap getMap(String name) {
        return pandora.getFireMap(name);
    }

    public FireList getList(String name) {
        return pandora.getFireList(name);
    }

    public FireRank getRank(String name) {
        return pandora.getFireRank(name);
    }

    public FireSet getSet(String name) {
        return pandora.getFireSet(name);
    }
}
