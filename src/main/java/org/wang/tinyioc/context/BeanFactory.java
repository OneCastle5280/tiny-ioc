package org.wang.tinyioc.context;

/**
 * bean 工厂，即 ioc 容器
 *
 * @author wangjiabao
 */
public interface BeanFactory {

    /**
     * 根据 beanName 获取 Bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
