package com.rakbow.kureakurusu.util.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;

/**
 * @author Rakbow
 * @since 2024/4/26 15:04
 */
public class MyBatisUtil {

    /**
     * <p>get table name by class with {@code TableName} annotation
     * @param clazz table entity class
     * @return {@code java.lang.String}
     * @since 2024/4/26 15:47
     */
    public static String getTableName(Class<?> clazz) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        return tableName.value();
    }

}
