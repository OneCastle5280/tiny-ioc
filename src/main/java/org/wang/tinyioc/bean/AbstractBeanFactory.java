package org.wang.tinyioc.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjiabao
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    /**
     * beanName -> instance
     */
    private final Map<String, Object> beanInstanceMap = new HashMap<>();

    @Override
    public void instantiateSingletons(String beanName, RootBeanDefinition beanDefinition) {
        createBean(beanName, beanDefinition);
    }

    @Override
    public void addBean(String beanName, Object bean) {
        this.beanInstanceMap.put(beanName, bean);
    }

    protected void createBean(String beanName, RootBeanDefinition beanDefinition) {
        try {
            this.doCreateBean(beanName, beanDefinition);
        } catch (Exception e) {
            // TODO
        }
    }

    protected void doCreateBean(String beanName, RootBeanDefinition beanDefinition) {
        // check had already created
        Object bean = getBean(beanName);
        if (bean != null) {
            return;
        }
        // post process before instantiation
        bean = invokePostProcessBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            // invoke bean post processor, after bean initialization
            invokePostProcessAfterInitialization(beanName, bean);
            return;
        }
        // instance bean
        bean = beanInstance(beanName, beanDefinition);
        // populate properties
        populateProperties(beanName, beanDefinition);
        // initialize bean
        initializeBean(bean, beanName, beanDefinition);
        // add bean instance to bean factory
        addBean(beanName, bean);
    }

    protected Object beanInstance(String beanName, RootBeanDefinition beanDefinition) {
        return null;
    }

    protected void populateProperties(String beanName, RootBeanDefinition beanDefinition) {
        return;
    }
    protected void initializeBean(Object bean, String beanName, RootBeanDefinition beanDefinition) {
        // invoke bean post processor, before bean initialization
        invokePostProcessBeforeInitialization(beanName, bean);
        // invoke bean init method
        invokeInitMethod();
        // invoke bean post processor, after bean initialization
        invokePostProcessAfterInitialization(beanName, bean);
    }

    protected void invokeInitMethod() {

    }


    /**
     * invoke post process before bean Instantiation, allow return proxy bean
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    protected Object invokePostProcessBeforeInstantiation(String beanName, RootBeanDefinition beanDefinition) {
        // TODO
        return null;
    }

    /**
     * invoke post process after bean Initialization
     */
    protected void invokePostProcessAfterInitialization(String beanName, Object bean) {
        // TODO
    }

    /**
     * invoke post process before bean Initialization
     */
    protected void invokePostProcessBeforeInitialization(String beanName, Object bean) {
        // TODO
    }
}
