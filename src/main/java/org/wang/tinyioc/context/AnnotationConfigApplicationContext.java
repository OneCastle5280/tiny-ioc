package org.wang.tinyioc.context;


import org.wang.tinyioc.bean.AnnotationBeanDefinition;
import org.wang.tinyioc.processor.ConfigurationConfigBeanDefinitionRegisterPostProcessor;

/**
 * application context for annotation config
 *
 * @author wangjiabao
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext  {

    /**
     * Configuration support
     */
    private static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.wang.tinyioc.processor.internalConfigurationAnnotationProcessor";

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        super();
        // register configuration bean to BeanDefinition
        register(configurationClass, new AnnotationBeanDefinition(configurationClass));
        // register ConfigurationConfigBeanDefinitionRegisterPostProcessor
        register(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, new AnnotationBeanDefinition(ConfigurationConfigBeanDefinitionRegisterPostProcessor.class));
        // refresh bean factory
        refresh();
    }
}
