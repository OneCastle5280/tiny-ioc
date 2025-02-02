package org.wang.tinyioc.processor;

import org.wang.tinyioc.bean.BeanFactory;

/**
 * @author wangjiabao
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {


    void postProcessBeanDefinitionRegistry(BeanFactory beanFactory) throws Exception;
}
