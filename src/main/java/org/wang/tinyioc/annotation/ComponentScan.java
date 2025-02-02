package org.wang.tinyioc.annotation;

import java.lang.annotation.*;

/**
 * scan {@link Component}
 *
 * @author wangjiabao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {
    /**
     * basePackages
     */
    String[] value() default {};
}
