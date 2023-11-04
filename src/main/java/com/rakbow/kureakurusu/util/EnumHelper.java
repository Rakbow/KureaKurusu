package com.rakbow.kureakurusu.util;

import java.lang.reflect.Method;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 2:37
 * @Description:
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

}
