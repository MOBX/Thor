/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.rpc.common;

import java.lang.annotation.*;

/**
 * 扩展点接口的标识。
 * <p />
 * 扩展点声明配置文件，格式修改。<br />
 * 以Protocol示例，配置文件META-INF/thor/com.xxx.Protocol内容：<br />
 * 由<br/>
 * 
 * <pre>
 * <code>com.foo.XxxProtocol
 *  com.foo.YyyProtocol</code>
 * </pre>
 * 
 * <br/>
 * 改成使用KV格式<br/>
 * 
 * <pre>
 * <code>xxx=com.foo.XxxProtocol
 *  yyy=com.foo.YyyProtocol
 * </code>
 * </pre>
 * 
 * <br/>
 * 原因：<br/>
 * 当扩展点的static字段或方法签名上引用了三方库， import java.lang.annotation.*; .mob.thor.common.extension.SPI}
 * 
 * @author william.liangf
 * @author ding.lid
 * @export
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension {

    /**
     * @deprecated
     */
    @Deprecated
	String value() default "";

}