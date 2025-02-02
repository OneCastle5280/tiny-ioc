package org.wang.tinyioc.bean;

import java.util.Map;

/**
 * Bean Definition 注册接口
 *
 * @author wangjiabao
 */
public interface BeanDefinitionRegister {

    /**
     * register BeanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * get BeanDefinition map
     */
    Map<String, BeanDefinition> getBeanDefinitionMap();

    /**
     * contain BeanDefinition
     */
    boolean containBeanDefinition(String beanName);
}
