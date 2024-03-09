package com.rakbow.kureakurusu.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rakbow
 * @since 2024/01/14 20:29
 */
@Retention(RetentionPolicy.CLASS)
@Mapping(source = "addedTime", target = "addedTime", qualifiedByName = "getVOTime")
@Mapping(source = "editedTime", target = "editedTime", qualifiedByName = "getVOTime")
public @interface ToVO {
}
