package org.wang.tinyioc.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjiabao
 */
public abstract class AbstractListableBeanFactory implements ListableBeanFactory {

    /**
     * beanName -> instance
     */
    private final Map<String, Object> beanInstanceMap = new HashMap<>();

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

    @Override
    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return this.beanDefinitionMap;
    }

    @Override
    public boolean containBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public void instantiateSingletons(String beanName, RootBeanDefinition beanDefinition) {
        createBean(beanName, beanDefinition);
    }

    @Override
    public void addBean(String beanName, Object bean) {
        this.beanInstanceMap.put(beanName, bean);
    }

    @Override
    public Object getBean(String beanName) {
        return this.beanInstanceMap.get(beanName);
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
