package org.wang.tinyioc.bean;

/**
 * @author wangjiabao
 */
public interface ObjectFactory<T> {

    /**
     * gen object
     */
    T getObject();
}
