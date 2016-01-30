/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common.compiler;

import com.mob.thor.rpc.common.extension.SPI;

/**
 * Compiler. (SPI, Singleton, ThreadSafe)
 * 
 * @author zxc Dec 31, 2015 6:21:21 PM
 */
@SPI("javassist")
public interface Compiler {

    /**
     * Compile java source code.
     * 
     * @param code Java source code
     * @param classLoader
     * @return Compiled class
     */
    Class<?> compile(String code, ClassLoader classLoader);
}
