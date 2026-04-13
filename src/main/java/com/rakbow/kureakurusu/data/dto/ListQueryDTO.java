package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * @author Rakbow
 * @since 2025/5/23 19:56
 */
@Slf4j
@Data
public abstract class ListQueryDTO {

    private int page;
    private int size;
    private int offset;
    private String sortField;
    private int sortOrder;
    private String groupField;
    private String keyword;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> filters = new LinkedHashMap<>();

    private static String VALUE_KEY = "value";

    public ListQueryDTO() {
        size = isPage() ? getSize() : -1;
        keyword = getVal("keyword");
    }

    /**
     * 自动初始化:通过反射将filters中的值映射到子类字段
     * 子类可以选择性重写此方法以处理特殊逻辑
     */
    public void init() {
        autoMapFilters();
    }

    /**
     * 自动将filters中的值映射到子类字段
     * 映射规则:
     * 1. 优先使用字段名作为key
     * 2. 支持常见命名转换(如listId -> list_id)
     */
    private void autoMapFilters() {
        Class<?> clazz = this.getClass();
        
        // 遍历当前类及其父类(直到ListQueryDTO)的所有字段
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            
            for (Field field : fields) {
                String fieldName = field.getName();
                
                // 跳过父类已定义的字段和静态字段
                if ("filters".equals(fieldName) || "VALUE_KEY".equals(fieldName) || 
                    java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                
                // 尝试从filters中获取值
                Object value = getVal(fieldName);
                
                // 如果直接匹配失败,尝试常见命名转换
                if (value == null) {
                    // 尝试驼峰转下划线
                    value = getVal(CommonUtil.camelToUnderline(fieldName));
                }
                
                // 如果找到值,设置到字段
                if (value != null) {
                    try {
                        field.setAccessible(true);
                        
                        // 处理类型转换
                        Object convertedValue = convertType(value, field.getType());
                        field.set(this, convertedValue);
                    } catch (IllegalAccessException e) {
                        log.warn("Failed to set field: {}", fieldName, e);
                    }
                }
            }
            
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 类型转换辅助方法
     */
    private Object convertType(Object value, Class<?> targetType) {
        if (value == null || targetType.isInstance(value)) {
            return value;
        }
        
        try {
            // Integer -> Long
            if (targetType == Long.class || targetType == long.class) {
                if (value instanceof Integer) {
                    return ((Integer) value).longValue();
                } else if (value instanceof Number) {
                    return ((Number) value).longValue();
                }
            }
            // String -> Integer
            else if (targetType == Integer.class || targetType == int.class) {
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                }
            }
            // 其他数值类型转换
            else if (targetType == Double.class || targetType == double.class) {
                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                }
            }
            else if (targetType == Float.class || targetType == float.class) {
                if (value instanceof Number) {
                    return ((Number) value).floatValue();
                }
            }
        } catch (Exception e) {
            log.warn("Type conversion failed for value: {} to type: {}", value, targetType.getSimpleName(), e);
        }
        
        return value;
    }

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtil.isNotBlank(this.sortField);
    }

    public boolean isPage() {
        return this.getSize() > 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T getVal(String key) {
        Object value = null;
        if(this.filters.containsKey(key)) {
            Object field = this.filters.get(key);
            if(field == null) return null;
            value = this.filters.get(key).get(VALUE_KEY);
        }
        if(value == null || value.toString().isEmpty())
            return null;
        return (T) value;
    }

    public String getSortField() {
        return CommonUtil.camelToUnderline(this.sortField);
    }

}
