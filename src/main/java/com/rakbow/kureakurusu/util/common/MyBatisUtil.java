package com.rakbow.kureakurusu.util.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.annotation.QueryColumnType;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.test.AlbumListVO;
import com.rakbow.kureakurusu.data.vo.test.ItemListVO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
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

    private final static Map<Integer, Class<? extends ItemListVO>> itemListVOMap = new HashMap<>() {{
        put(Entity.ALBUM.getValue(), AlbumListVO.class);
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

    public static Class<? extends ItemListVO> getItemListVO(int type) {
        return itemListVOMap.get(type);
    }

    @SneakyThrows
    public static void itemListQueryWrapper(ItemListQueryDTO dto, MPJLambdaWrapper<Item> wrapper) {
        Class<? extends ItemListQueryDTO> queryClazz = dto.getClass();
        for (Field field : queryClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(QueryColumn.class)) {
                QueryColumn column = field.getAnnotation(QueryColumn.class);
                field.setAccessible(true);
                Object value = field.get(dto);
                String colName = column.name();
                if(StringUtils.isBlank(column.name()))
                    colName = CommonUtil.camelToUnderline(field.getName());
                if (value == null || value.toString().isEmpty()) continue;
                if (column.type() == QueryColumnType.STRING) {
                    wrapper.like(STR."t1.\{colName}", value);
                } else if (column.type() == QueryColumnType.NUMBER_LIST) {
                    wrapper.apply(STR."JSON_CONTAINS(t1.\{colName}, '\{JsonUtil.toJson(value)}')");
                } else if (column.type() == QueryColumnType.BOOLEAN) {
                    wrapper.eq(STR."t1.\{colName}", value);
                }
            }
        }
    }

}
