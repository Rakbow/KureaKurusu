package com.rakbow.kureakurusu.toolkit.converter;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.util.I18nHelper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/5 8:08
 */
@Component
public class EnumToAttributes {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <X, T extends Enum<T>> List<Attribute<X>> toAttributes(List<T> list) {
        List<Attribute<X>> res = new ArrayList<>();
        for(T t : list) {
            Method getValueMethod = t.getClass().getMethod("getValue");
            Method getLabelMethod = t.getClass().getMethod("getLabelKey");
            String labelKey = getLabelMethod.invoke(t).toString();
            String label = I18nHelper.getMessage(labelKey);
            X value = (X) getValueMethod.invoke(t);
            res.add(new Attribute<>(label, value));
        }
        return res;
    }

}