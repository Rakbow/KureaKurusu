package com.rakbow.kureakurusu.data.emun;

import com.rakbow.kureakurusu.data.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-05-19 19:02
 */
public interface MetaEnum {

    Integer getValue();
    String getLabelKey();

    // 通过value获取枚举实例的通用方法
    static <T extends Enum<T> & MetaEnum> T get(Class<T> enumClass, int value) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getValue().equals(value)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException(STR."Invalid value: \{value}");
    }

}
