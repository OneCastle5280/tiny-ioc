package org.wang.tinyioc.processor;

/**
 * @author wangjiabao
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * post-process before instantiation
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);
}
