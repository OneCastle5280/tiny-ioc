package org.wang.tinyioc.context;


import org.wang.tinyioc.bean.AnnotationBeanDefinition;

/**
 * application context for annotation config
 *
 * @author wangjiabao
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext  {

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        super();
        // register configuration bean to BeanDefinition
        register(configurationClass, new AnnotationBeanDefinition(configurationClass));
        // refresh bean factory
        refresh();
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
