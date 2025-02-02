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
     * add bean to beanFactory
     */
    void addBean(String beanName, Object bean);

    /**
     * instantiate singleton bean
     */
    void instantiateSingletons(String beanName, RootBeanDefinition beanDefinition);
}
