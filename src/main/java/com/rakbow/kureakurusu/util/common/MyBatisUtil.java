package com.rakbow.kureakurusu.util.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/4/26 15:04
 */
public class MyBatisUtil {

    private final static Map<Integer, Class<? extends SubItem>> subItemMap = new HashMap<>() {{
        put(Entity.ALBUM.getValue(), ItemAlbum.class);
        put(Entity.BOOK.getValue(), ItemBook.class);
    }};
    private final static Map<Integer, Class<? extends SuperItem>> SuperItemMap = new HashMap<>() {{
        put(Entity.ALBUM.getValue(), Album.class);
        put(Entity.BOOK.getValue(), Book.class);
    }};

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

    public static <T, S> BaseMapper<S> getMapper(Class<T> clazz) {
        String key = StringUtils.uncapitalize(clazz.getSimpleName());
        return SpringUtil.getBean(STR."\{key}Mapper");
    }

    public static Class<? extends SubItem> getSubItem(int type) {
        return subItemMap.get(type);
    }

    public static Class<? extends SuperItem> getSuperItem(int type) {
        return SuperItemMap.get(type);
    }

}
