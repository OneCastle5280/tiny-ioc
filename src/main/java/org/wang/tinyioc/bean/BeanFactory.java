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
}
