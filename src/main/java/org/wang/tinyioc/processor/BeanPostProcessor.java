package org.wang.tinyioc.processor;

/**
 * bean post processor
 *
 * @author wangjiabao
 */
public interface BeanPostProcessor {

    /**
     * post process before initialization, after bean populate property, but before invoke init method
     */
    void postProcessBeforeInitialization(Object bean, String beanName);

    /**
     * post process after initialization, after bean populate property, but before invoke init method
     */
    void postProcessAfterInitialization(Object bean, String beanName);
}
