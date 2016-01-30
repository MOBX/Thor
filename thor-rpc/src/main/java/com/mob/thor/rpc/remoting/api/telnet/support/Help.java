package com.mob.thor.rpc.remoting.api.telnet.support;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Help {

    String parameter() default "";

    String summary();

    String detail() default "";

}
