package com.rakbow.kureakurusu.annotation;

import java.lang.annotation.*;

/**
 * @author Rakbow
 * @since 2023-02-21 22:18
 */
@Documented   //该注解表示支持javaDoc文档导出
@Retention(RetentionPolicy.RUNTIME) //该注解表示生命周期
@Target(ElementType.METHOD)  //该注解表示自定义的注解可以使用的对象
public @interface UniqueVisitor {
}
