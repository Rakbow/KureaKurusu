package com.rakbow.kureakurusu.annotation;

import com.rakbow.kureakurusu.data.enums.PermissionLogical;

import java.lang.annotation.*;

/**
 * @author Rakbow
 * @since 2026/3/14 12:44
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    String[] value();
    PermissionLogical logical() default PermissionLogical.AND;

}
