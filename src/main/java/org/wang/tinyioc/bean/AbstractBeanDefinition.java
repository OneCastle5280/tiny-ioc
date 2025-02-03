package org.wang.tinyioc.bean;

import lombok.Data;
import org.wang.tinyioc.utils.BeanNameGenUtils;

/**
 * @author wangjiabao
 */
@Data
public abstract class AbstractBeanDefinition implements BeanDefinition {
    /**
     * beanName
     */
    private String beanName;
    /**
     * the class of bean
     */
    private Class<?> beanClass;
    /**
     * {@link BeanDefinition#SCOPE_SINGLETON}
     */
    private String Scope;

    public AbstractBeanDefinition(Class<?> clazz) {
        this.Scope = BeanDefinition.SCOPE_SINGLETON;
        this.beanClass = clazz;
        this.beanName = BeanNameGenUtils.parseBeanName(clazz);
    }
}
