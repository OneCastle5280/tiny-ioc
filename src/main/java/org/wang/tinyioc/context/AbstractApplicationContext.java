package org.wang.tinyioc.context;

import org.wang.tinyioc.bean.*;
import org.wang.tinyioc.processor.BeanFactoryPostProcessor;
import org.wang.tinyioc.utils.BeanNameGenUtils;

import java.util.ArrayList;
import java.util.List;

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
            // prepare bean factory
            prepareBeanFactory(beanFactory);
            // allow post process bean factory
            postProcessBeanFactory(beanFactory);
            // invoke BeanFactoryPostProcessor
            invokeBeanFactoryPostProcessors(beanFactory);
            // register BeanPostProcessors
            registerBeanPostProcessor(beanFactory);
            // TODO Listener
            // beanFactory initialization
            beanFactoryInitialization(beanFactory);
            // finish
            finishRefresh();
        } catch (Exception e) {

        }
    }

    protected void finishRefresh() {
        // TODO
    }

    /**
     * initialize bean factory
     */
    protected void beanFactoryInitialization(BeanFactory beanFactory) {
        // 实例前置处理器
        // 实例化
        // 属性值依赖注入
        // 后置处理器
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
}
