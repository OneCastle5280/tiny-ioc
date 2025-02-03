package org.wang.tinyioc.bean;

/**
 * bean factory
 *
 * @author wangjiabao
 */
public interface BeanFactory {

    /**
     * get bean by beanName
     */
    Object getBean(String beanName);

    /**
     * get bean by type
     */
    Object getBean(Class<?> clazz);

    /**
     * add bean to beanFactory
     */
    void addBean(String beanName, Object bean);

    /**
     * instantiate singleton bean
     */
    void instantiateSingletons(String beanName, RootBeanDefinition beanDefinition);
}
