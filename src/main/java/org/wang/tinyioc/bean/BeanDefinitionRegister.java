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
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * find BeanDefinition by type
     */
    List<BeanDefinition> findByType(Class<?> clazz);

    /**
     * find BeanDefinition by name
     */
    BeanDefinition findByName(String beanName);

    /**
     * get BeanDefinition map
     */
    Map<String, BeanDefinition> getBeanDefinitionMap();

    /**
     * contain BeanDefinition
     */
    boolean containBeanDefinition(String beanName);
}
