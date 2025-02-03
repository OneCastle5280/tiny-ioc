package org.wang.tinyioc.context;

import org.wang.tinyioc.bean.*;
import org.wang.tinyioc.processor.BeanDefinitionRegistryPostProcessor;
import org.wang.tinyioc.processor.BeanFactoryPostProcessor;
import org.wang.tinyioc.processor.BeanPostProcessor;
import org.wang.tinyioc.utils.BeanNameGenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangjiabao
 */
public abstract class AbstractApplicationContext extends AbstractListableBeanFactory {

    /**
     * bean factory
     */
    private final ListableBeanFactory beanFactory;

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    public void register(Class<?> clazz, BeanDefinition beanDefinition) {
        String beanName = BeanNameGenUtils.parseBeanName(clazz);
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    public void register(String beanName, BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
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
            ListableBeanFactory beanFactory = getBeanFactory();
            // prepare bean factory
            prepareBeanFactory(beanFactory);
            // invoke BeanFactoryPostProcessor
            invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
            // register BeanPostProcessors
            registerBeanPostProcessor(beanFactory, getBeanPostProcessors());
            // TODO listener
            // beanFactory initialization
            beanFactoryInitialization(beanFactory, beanFactory);
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

    protected void invokeBeanFactoryPostProcessors(ListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) throws Exception {
        if (beanFactoryPostProcessors == null || beanFactoryPostProcessors.isEmpty()) {
            return;
        }

        final List<BeanDefinitionRegistryPostProcessor> registerBeanFactoryPostProcessors = new ArrayList<>();
        final List<BeanFactoryPostProcessor> postProcessors = new ArrayList<>();

        // invoke BeanFactoryPostProcessors
        for (BeanFactoryPostProcessor processor : beanFactoryPostProcessors) {
            if (processor instanceof BeanDefinitionRegistryPostProcessor) {
                registerBeanFactoryPostProcessors.add((BeanDefinitionRegistryPostProcessor) processor);
            }
            postProcessors.add(processor);
        }
        // TODO sort processors
        // invoke postProcessBeanDefinitionRegistry
        for (BeanDefinitionRegistryPostProcessor registryPostProcessor : registerBeanFactoryPostProcessors) {
            registryPostProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        }

        // invoke postProcessBeanFactory
        for (BeanFactoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    protected void registerBeanPostProcessor(ListableBeanFactory beanFactory, List<BeanPostProcessor> beanPostProcessors) {
        // find all BeanPostProcessors Definition
        List<BeanDefinition> beanPostProcessDefinitionList = beanFactory.findByType(BeanPostProcessor.class);
        if (beanPostProcessDefinitionList == null || beanPostProcessDefinitionList.isEmpty()) {
            return;
        }

        for (BeanDefinition beanDefinition : beanPostProcessDefinitionList) {
            if (beanDefinition instanceof RootBeanDefinition) {
                RootBeanDefinition rbd = (RootBeanDefinition) beanDefinition;
                // get from beanFactory
                Object postProcessor = beanFactory.getBean(rbd.getBeanName());
                // register
                beanFactory.addBeanPostProcessor((BeanPostProcessor) postProcessor);
            }
        }
    }

    /**
     * prepare bean definition register
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
    protected ListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }
}
