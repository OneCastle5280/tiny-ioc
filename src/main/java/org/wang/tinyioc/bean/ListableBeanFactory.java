package org.wang.tinyioc.bean;


import org.wang.tinyioc.processor.BeanFactoryPostProcessor;
import org.wang.tinyioc.processor.BeanPostProcessor;

import java.util.List;

/**
 * extends {@link BeanFactory} {@link BeanDefinitionRegister}
 *
 * @author wangjiabao
 */
public interface ListableBeanFactory extends BeanFactory, BeanDefinitionRegister{

    /**
     * get beanFactoryPostProcessors
     */
    List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors();

    /**
     * add beanFactoryPostProcessor
     */
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    /**
     * get beanPostProcessors
     */
    List<BeanPostProcessor> getBeanPostProcessors();

    /**
     * add beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
