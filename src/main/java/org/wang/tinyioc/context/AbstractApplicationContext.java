package org.wang.tinyioc.context;

import org.wang.tinyioc.bean.*;
import org.wang.tinyioc.processor.BeanFactoryPostProcessor;
import org.wang.tinyioc.utils.BeanNameGenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangjiabao
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    /**
     * bean factory
     */
    private final BeanFactory beanFactory;

    /**
     * bean definition register
     */
    private final BeanDefinitionRegister beanDefinitionRegister;

    /**
     * bean factory post processors
     */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>(16);

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultBeanFactory();
        this.beanDefinitionRegister = new DefaultBeanDefinitionRegister();
    }

    public void register(Class<?> clazz, BeanDefinition beanDefinition) {
        String beanName = BeanNameGenUtils.parseBeanName(clazz);
        this.beanDefinitionRegister.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * this method is used to refresh the application context
     */
    public void refresh() {
        synchronized (this) {
            this.doRefresh();
        }
    }

    /**
     * actual refresh the application context
     */
    private void doRefresh() {
        try {
            // prepare refresh
            prepareRefresh();
            // get bean factory
            BeanFactory beanFactory = getBeanFactory();
            // get bean definition register
            BeanDefinitionRegister beanDefinitionRegister = getBeanDefinitionRegister();
            // prepare bean factory
            prepareBeanFactory(beanFactory);
            // allow post process bean factory
            postProcessBeanFactory(beanFactory);
            // invoke BeanFactoryPostProcessor
            invokeBeanFactoryPostProcessors(beanFactory);
            // register BeanPostProcessors
            registerBeanPostProcessor(beanFactory);
            // TODO listener
            // beanFactory initialization
            beanFactoryInitialization(beanFactory, beanDefinitionRegister);
            // finish
            finishRefresh();
        } catch (Exception e) {
            // TODO
        }
    }

    protected void finishRefresh() {
        // TODO
    }

    /**
     * initialize bean factory
     */
    protected void beanFactoryInitialization(BeanFactory beanFactory, BeanDefinitionRegister beanDefinitionRegister) {
        Map<String, BeanDefinition> beanDefinitionMap = beanDefinitionRegister.getBeanDefinitionMap();
        if (beanDefinitionMap == null || beanDefinitionMap.isEmpty()) {
            return;
        }
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition instanceof RootBeanDefinition) {
                RootBeanDefinition rootBeanDefinition = (RootBeanDefinition) beanDefinition;
                // initialize bean
                beanFactory.instantiateSingletons(beanName, rootBeanDefinition);
            }
        });
    }

    protected void postProcessBeanFactory(BeanFactory beanFactory) {
        // For subclasses: do nothing by default.
    }

    protected void invokeBeanFactoryPostProcessors(BeanFactory beanFactory) {
        // TODO
    }

    protected void registerBeanPostProcessor(BeanFactory beanFactory) {
        // TODO
    }

    /**
     * Prepare bean factory
     */
    protected void prepareBeanFactory(BeanFactory beanFactory) {
        // TODO
    }

    /**
     * Prepare this context for refreshing
     */
    protected void prepareRefresh() {
        // For subclasses: do nothing by default.
    }

    /**
     * get bean factory
     */
    private BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    /**
     * get BeanFactoryPostProcessors {@link BeanFactoryPostProcessor}
     */
    private List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }


    private BeanDefinitionRegister getBeanDefinitionRegister() {
        return this.beanDefinitionRegister;
    }
}
