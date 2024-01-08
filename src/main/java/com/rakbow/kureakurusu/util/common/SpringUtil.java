package com.rakbow.kureakurusu.util.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Spring工具类
 *
 * @author Rakbow
 * @since 2023-01-10 11:17
 */

@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    private static MessageSource messageSource;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void set(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 通过字节码获取
     *
     * @author Rakbow
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * 通过BeanName获取
     *
     * @author Rakbow
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 通过beanName和字节码获取
     *
     * @author Rakbow
     */
    public static <T> T getBean(String name, Class<T> beanClass) {
        return context.getBean(name, beanClass);
    }

    public static MessageSource getMessageSource() {
        if (messageSource == null) {
            messageSource = getBean("messageSource");
        }
        return messageSource;
    }
}
