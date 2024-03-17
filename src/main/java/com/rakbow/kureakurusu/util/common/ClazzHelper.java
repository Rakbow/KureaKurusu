package com.rakbow.kureakurusu.util.common;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author Rakbow
 * @since 2024/3/17 1:42
 */
public class ClazzHelper {

    public static String getColumnName(String propertyName) {
        if(StringUtils.isNotBlank(propertyName))
            return propertyName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        return null;
    }
}
