package org.wang.tinyioc.bean;

/**
 * Bean Definition 注册接口
 *
 * @author wangjiabao
 */
public interface BeanDefinitionRegister {

    /**
     * 注册 BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
