package org.wang.tinyioc.bean;

import lombok.Getter;
import org.wang.tinyioc.annotation.AutoInject;
import org.wang.tinyioc.processor.BeanFactoryPostProcessor;
import org.wang.tinyioc.processor.BeanPostProcessor;
import org.wang.tinyioc.processor.InstantiationAwareBeanPostProcessor;
import org.wang.tinyioc.utils.BeanNameGenUtils;
import sun.reflect.misc.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wangjiabao
 */
public abstract class AbstractListableBeanFactory implements ListableBeanFactory {

    /**
     * beanName -> instance
     */
    private final Map<String, Object> beanInstanceMap = new HashMap<>();

    /**
     * beanName -> instance
     */
    private final Map<String, Object> earlyInstanceMap = new HashMap<>();

    /**
     * beanName -> ObjectFactory
     */
    private final Map<String, ObjectFactory<?>> instanceFactoryMap = new HashMap<>();

    /**
     * key -> value: beanName -> BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(16);

    /**
     * bean factory post processors
     */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>(16);

    /**
     * bean post processors
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>(16);

    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        getBeanFactoryPostProcessors().add(beanFactoryPostProcessor);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanPostProcessors().add(beanPostProcessor);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (getBeanDefinitionMap().containsKey(beanName)) {
            throw new IllegalStateException("BeanDefinition [" + beanName + "] already exists");
        }
        // register
        getBeanDefinitionMap().put(beanName, beanDefinition);
    }

    @Override
    public List<BeanDefinition> findByType(Class<?> clazz) {
       return getBeanDefinitionMap().values().stream()
               .filter(item -> item.getClass().equals(clazz))
               .collect(Collectors.toList());
    }

    @Override
    public BeanDefinition findByName(String beanName) {
        Map<String, BeanDefinition> map = getBeanDefinitionMap();
        String targetBeanName = map.keySet().stream().filter(item -> item.equals(beanName)).findFirst().orElse(null);
        return map.get(targetBeanName);
    }

    @Override
    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return this.beanDefinitionMap;
    }

    @Override
    public boolean containBeanDefinition(String beanName) {
        return getBeanDefinitionMap().containsKey(beanName);
    }

    @Override
    public void instantiateSingletons(String beanName, RootBeanDefinition beanDefinition) {
        createBean(beanName, beanDefinition);
    }

    @Override
    public void addBean(String beanName, Object bean) {
        getBeanInstanceMap().put(beanName, bean);
    }

    @Override
    public Object getBean(String beanName) {
        Object bean = getBeanInstanceMap().get(beanName);
        if (bean != null) {
            return bean;
        }
        // if bean in creating
        if (isInCreating(beanName)) {
            // find from earlyInstanceMap
            Map<String, Object> earlyInstanceMap = getEarlyInstanceMap();
            if (earlyInstanceMap.containsKey(beanName)) {
                return earlyInstanceMap.get(beanName);
            } else if (getInstanceFactoryMap().containsKey(beanName)) {
                Map<String, ObjectFactory<?>> instanceFactoryMap = getInstanceFactoryMap();
                ObjectFactory<?> objectFactory = instanceFactoryMap.get(beanName);
                if (objectFactory != null) {
                    bean = objectFactory.getObject();
                    // remove from instanceFactoryMap
                    instanceFactoryMap.remove(beanName);
                    // put earlyInstanceMap
                    earlyInstanceMap.put(beanName, bean);
                    return bean;
                }
            }
        }
        // final create bean
        return createBean(beanName, (RootBeanDefinition) findByName(beanName));
    }

    protected boolean isInCreating(String beanName) {
        return getInstanceFactoryMap().containsKey(beanName);
    }

    @Override
    public Object getBean(Class<?> clazz) {
        String beanName = BeanNameGenUtils.parseBeanName(clazz);
        return getBean(beanName);
    }

    protected Map<String, Object> getBeanInstanceMap() {
        return this.beanInstanceMap;
    }

    protected Map<String, Object> getEarlyInstanceMap() {
        return this.earlyInstanceMap;
    }

    protected Map<String, ObjectFactory<?>> getInstanceFactoryMap() {
        return this.instanceFactoryMap;
    }

    protected Object createBean(String beanName, RootBeanDefinition beanDefinition) {
        try {
            return this.doCreateBean(beanName, beanDefinition);
        } catch (Exception e) {
            // TODO
        }
        return null;
    }

    protected Object doCreateBean(String beanName, RootBeanDefinition beanDefinition) throws Exception {
        // check had already created
        Object bean = getBean(beanName);
        if (bean != null) {
            return bean;
        }
        // post process before instantiation
        bean = invokePostProcessBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            // invoke bean post processor, after bean initialization
            invokePostProcessAfterInitialization(beanName, bean);
            return bean;
        }
        // instance bean
        bean = beanInstance(beanDefinition);
        // put objectFactory to instanceFactoryMap
        getInstanceFactoryMap().put(beanName, () -> createBean(beanName, beanDefinition));
        // populate properties
        populateProperties(bean);
        // initialize bean
        initializeBean(bean, beanName, beanDefinition);
        // add bean instance to bean factory
        addBean(beanName, bean);
        return bean;
    }

    protected Object beanInstance(RootBeanDefinition beanDefinition) throws Exception {
        return ReflectUtil.newInstance(beanDefinition.getBeanClass());
    }

    /**
     * auto-inject properties
     */
    protected void populateProperties(Object bean) throws Exception {
        Class<?> beanClass = bean.getClass();
        // find AutoInject annotation
        for (Field field : beanClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInject.class)) {
                autoInjectField(bean, field);
            }
        }
    }

    protected void autoInjectField(Object bean, Field field) throws Exception {
        Object dependency = getBean(field.getClass());
        if (dependency == null) {
            return;
        }
        field.setAccessible(true);
        field.set(bean, dependency);
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
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                Object bean = ((InstantiationAwareBeanPostProcessor) processor).postProcessBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
                if (bean != null) {
                    return bean;
                }
            }
        }
        return null;
    }

    /**
     * invoke post process before bean Initialization
     */
    protected void invokePostProcessBeforeInitialization(String beanName, Object bean) {
        getBeanPostProcessors()
                .forEach(processor ->
                        processor.postProcessBeforeInitialization(bean, beanName)
                );
    }

    /**
     * invoke post process after bean Initialization
     */
    protected void invokePostProcessAfterInitialization(String beanName, Object bean) {
        getBeanPostProcessors()
                .forEach(processor ->
                        processor.postProcessAfterInitialization(bean, beanName)
                );
    }
}
