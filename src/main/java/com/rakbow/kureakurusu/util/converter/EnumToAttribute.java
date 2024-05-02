package com.rakbow.kureakurusu.util.converter;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.util.I18nHelper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Rakbow
 * @since 2024/4/29 22:18
 */
@Component
public class EnumToAttribute {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <X, T extends Enum<T>> Attribute<X> toAttribute(T t) {
        Method getValueMethod = t.getClass().getMethod("getValue");
        Method getLabelMethod = t.getClass().getMethod("getLabelKey");
        String labelKey = getLabelMethod.invoke(t).toString();
        String label = I18nHelper.getMessage(labelKey);
        X value = (X) getValueMethod.invoke(t);
        return new Attribute<>(label, value);
    }

}
