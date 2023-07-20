package com.rakbow.kureakurusu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-09-30 9:49
 * @Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAuthorityRequired {
}
