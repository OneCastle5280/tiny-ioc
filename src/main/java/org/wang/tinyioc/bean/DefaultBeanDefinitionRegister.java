package org.wang.tinyioc.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link BeanDefinitionRegister} default implementation.
 *
 * @author wangjiabao
 */
public class DefaultBeanDefinitionRegister extends AbstractBeanDefinitionRegister{

    /**
     * key -> value: beanName -> BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(16);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinitionMap.containsKey(beanName)) {
            throw new IllegalStateException("BeanDefinition [" + beanName + "] already exists");
        }
        // register
        beanDefinitionMap.put(beanName, beanDefinition);
    }
}
