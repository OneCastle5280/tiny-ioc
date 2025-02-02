package org.wang.tinyioc.bean;

/**
 * bean 工厂，即 ioc 容器
 *
 * @author wangjiabao
 */
public interface BeanFactory {

    /**
     * get bean by beanName
     *
     * @param beanName
     * @return
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
