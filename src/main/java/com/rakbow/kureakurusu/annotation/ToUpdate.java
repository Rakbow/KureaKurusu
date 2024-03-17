package com.rakbow.kureakurusu.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rakbow
 * @since 2024/3/17 19:05
 */
@Retention(RetentionPolicy.CLASS)
@Mapping(target = "images",  constant = "null")
@Mapping(target = "detail",  constant = "null")
@Mapping(target = "addedTime",  constant = "null")
@Mapping(target = "status",  constant = "null")
public @interface ToUpdate {
}
