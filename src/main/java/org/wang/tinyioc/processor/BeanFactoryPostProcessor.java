package org.wang.tinyioc.processor;

import org.wang.tinyioc.bean.BeanFactory;

/**
 * bean factory post processor
 *
 * @author wangjiabao
 */
public interface BeanFactoryPostProcessor {

    /**
     * post process after bean factory init, but before bean instance
     */
    void postProcessBeanFactory(BeanFactory beanFactory);
}
