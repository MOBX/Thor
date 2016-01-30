/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common.compiler.support;

import com.mob.thor.rpc.common.extension.Adaptive;
import com.mob.thor.rpc.common.extension.ExtensionLoader;

/**
 * AdaptiveCompiler. (SPI, Singleton, ThreadSafe)
 * 
 * @author william.liangf
 */
@Adaptive
public class AdaptiveCompiler implements com.mob.thor.rpc.common.compiler.Compiler {

    private static volatile String DEFAULT_COMPILER;

    public static void setDefaultCompiler(String compiler) {
        DEFAULT_COMPILER = compiler;
    }

    public Class<?> compile(String code, ClassLoader classLoader) {
        com.mob.thor.rpc.common.compiler.Compiler compiler;
        ExtensionLoader<com.mob.thor.rpc.common.compiler.Compiler> loader = ExtensionLoader.getExtensionLoader(com.mob.thor.rpc.common.compiler.Compiler.class);
        String name = DEFAULT_COMPILER; // copy reference
        if (name != null && name.length() > 0) {
            compiler = loader.getExtension(name);
        } else {
            compiler = loader.getDefaultExtension();
        }
        return compiler.compile(code, classLoader);
    }
}
