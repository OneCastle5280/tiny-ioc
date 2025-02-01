package org.wang.tinyioc.bean;

import lombok.Data;

/**
 * @author wangjiabao
 */
@Data
public abstract class AbstractBeanDefinition implements BeanDefinition {
    /**
     * the class of bean
     */
    private Class<?> beanClass;
    /**
     * {@link BeanDefinition#SCOPE_SINGLETON}
     */
    private String Scope;

    public AbstractBeanDefinition(Class<?> clazz) {
        this.Scope = BeanDefinition.SCOPE_SINGLETON;
        this.beanClass = clazz;
    }
}
