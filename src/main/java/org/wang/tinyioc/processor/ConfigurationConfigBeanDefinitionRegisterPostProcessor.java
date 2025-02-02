package org.wang.tinyioc.processor;

import org.wang.tinyioc.annotation.ComponentScan;
import org.wang.tinyioc.annotation.Configuration;
import org.wang.tinyioc.bean.AnnotationBeanDefinition;
import org.wang.tinyioc.bean.BeanFactory;
import org.wang.tinyioc.annotation.Component;
import org.wang.tinyioc.bean.ListableBeanFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * scan {@link Component} to BeanDefinitionRegister
 *
 * @author wangjiabao
 */
public class ConfigurationConfigBeanDefinitionRegisterPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private ListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(BeanFactory beanFactory) {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanFactory beanFactory) throws Exception {
        if (beanFactory instanceof ListableBeanFactory) {
            this.beanFactory = (ListableBeanFactory) beanFactory;
            // find all configuration BeanDefinition
            List<AnnotationBeanDefinition> configurationBeanDefinitions = findConfigurationBeanDefinition();
            for (AnnotationBeanDefinition configurationBeanDefinition : configurationBeanDefinitions) {
                // handle ComponentScan annotation
                handleComponentScanAnnotation(configurationBeanDefinition);
                // handle Bean annotation
                handleBeanAnnotation(configurationBeanDefinition);
            }
        }
    }

    private List<AnnotationBeanDefinition> findConfigurationBeanDefinition() {
        List<AnnotationBeanDefinition> result = new ArrayList<>();
        if (beanFactory == null) {
            return result;
        }

        // find all with configuration annotation BeanDefinition
        beanFactory.getBeanDefinitionMap().forEach((beanName, beanDefinition) -> {
            if (beanDefinition instanceof AnnotationBeanDefinition) {
                AnnotationBeanDefinition annotationBeanDefinition = (AnnotationBeanDefinition) beanDefinition;
                if (annotationBeanDefinition.getBeanClass().isAnnotationPresent(Configuration.class)) {
                    result.add(annotationBeanDefinition);
                }
            }
        });

        return result;
    }

    private void handleComponentScanAnnotation(AnnotationBeanDefinition beanDefinition) throws Exception {
        if (beanDefinition == null || beanDefinition.getBeanClass() == null) {
            return;
        }
        ComponentScan scan = beanDefinition.getBeanClass().getAnnotation(ComponentScan.class);
        if (scan == null) {
            return;
        }
        Set<Class<?>> componentClassSet = new HashSet<>();
        for (String basePackage : scan.value()) {
            Set<Class<?>> classes = scanComponentClass(basePackage);
            componentClassSet.addAll(classes);
        }

        componentClassSet.forEach(clazz -> {
            AnnotationBeanDefinition annotationBeanDefinition = new AnnotationBeanDefinition(clazz);
            // register component
            beanFactory.registerBeanDefinition(clazz.getName(), annotationBeanDefinition);
        });
    }

    private Set<Class<?>> scanComponentClass(String basePackage) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = convertPackageToPath(basePackage);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".equals(resource.getProtocol())) {
                File dir = new File(resource.toURI());
                scanDirectory(dir, basePackage, classes);
            }
        }
        return classes;
    }

    private void scanDirectory(File dir, String packageName, Set<Class<?>> classes) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                // recursively scans subdirectories
                scanDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                // convert to class name and load
                String className = packageName + '.' + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    // TODO
                }
            }
        }
    }

    private String convertPackageToPath(String basePackage) {
        return basePackage.replace('.', '/');
    }

    private void handleBeanAnnotation(AnnotationBeanDefinition beanDefinition) {
        // TODO
    }
}
