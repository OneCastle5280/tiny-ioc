package org.wang.tinyioc.bean;

import java.util.List;
import java.util.Map;

/**
 * Bean Definition 注册接口
 *
 * @author wangjiabao
 */
public interface BeanDefinitionRegister {

    /**
     * register BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * get BeanDefinition map
     */
    Map<String, BeanDefinition> getBeanDefinitionMap();
}
