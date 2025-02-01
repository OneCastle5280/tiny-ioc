package org.wang.tinyioc.utils;

/**
 * @author wangjiabao
 */
public class BeanNameGenUtils {

    /**
     * generate beanName from Class
     *
     * @param clazz
     * @return
     */
    public static String parseBeanName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
