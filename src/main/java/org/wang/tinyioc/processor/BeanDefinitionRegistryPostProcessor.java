package org.wang.tinyioc.processor;

import org.wang.tinyioc.bean.BeanFactory;

/**
 * @author wangjiabao
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    /**
     * post-process bean definition registry
     */
    void postProcessBeanDefinitionRegistry(BeanFactory beanFactory) throws Exception;
}
