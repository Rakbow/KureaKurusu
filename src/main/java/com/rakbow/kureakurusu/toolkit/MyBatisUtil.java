package com.rakbow.kureakurusu.toolkit;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.entity.item.Item;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

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

    public static <T, S> BaseMapper<S> getMapper(Class<T> clazz) {
        String key = StringUtils.uncapitalize(clazz.getSimpleName());
        return SpringUtil.getBean(STR."\{key}Mapper");
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
                } else if (column.type() == QueryColumnType.NUMBER) {
                    wrapper.eq(STR."t1.\{colName}", value);
                } else if (column.type() == QueryColumnType.NUMBER_LIST) {
                    wrapper.apply(STR."JSON_CONTAINS(t1.\{colName}, '\{JsonUtil.toJson(value)}')");
                } else if (column.type() == QueryColumnType.BOOLEAN) {
                    wrapper.eq(STR."t1.\{colName}", value);
                }
            }
        }
    }

}
