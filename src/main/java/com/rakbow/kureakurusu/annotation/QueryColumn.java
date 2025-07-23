package com.rakbow.kureakurusu.annotation;

import com.rakbow.kureakurusu.data.emun.QueryColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rakbow
 * @since 2024/5/5 6:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryColumn {
    String name() default "";
    QueryColumnType type() default QueryColumnType.STRING;
}