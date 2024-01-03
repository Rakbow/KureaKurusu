package com.rakbow.kureakurusu.util;

import com.rakbow.kureakurusu.data.Attribute;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-11-05 2:37
 */
public class EnumHelper {

    public static <T extends Enum<T>> String getMethodResultFromEnum(T enumValue, String methodName) {
        // 调用枚举实例的 methodName 方法
        try {
            Method getValueMethod = enumValue.getClass().getMethod(methodName);
            Object result = getValueMethod.invoke(enumValue);
            return result.toString();
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static <T extends Enum<T>> List<Attribute<Integer>> getAttributeOptions(Class<T> clazz, String lang) {
        List<Attribute<Integer>> attributes = new ArrayList<>();
        for (T enumValue : clazz.getEnumConstants()) {
            try {
                Field valueField = clazz.getDeclaredField("value");
                valueField.setAccessible(true);
                Integer value = (Integer) valueField.get(enumValue);

                // 获取 getLabelKey 方法
                Method getLabelKeyMethod = clazz.getDeclaredMethod("getLabelKey");

                // 调用 getLabelKey 方法
                Object result = getLabelKeyMethod.invoke(enumValue);

                // 将结果转换为字符串
                String labelKey = result.toString();

                String label = I18nHelper.getMessage(labelKey, lang);

                Attribute<Integer> attribute = new Attribute<>(label, value);

                attributes.add(attribute);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                // handle exception
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return attributes;
    }

}
