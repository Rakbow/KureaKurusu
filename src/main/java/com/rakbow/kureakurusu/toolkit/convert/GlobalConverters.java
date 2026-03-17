package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.SneakyThrows;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/7/12 11:51
 */
@Component
public class GlobalConverters {

    public String convert(Timestamp ts) {
        return DateHelper.timestampToString(ts);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <X, E extends Enum<E>> Attribute<X> toAttribute(E e) {
        Method getValueMethod = e.getClass().getMethod("getValue");
        Method getLabelMethod = e.getClass().getMethod("getLabelKey");
        String labelKey = getLabelMethod.invoke(e).toString();
        String label = I18nHelper.getMessage(labelKey);
        X value = (X) getValueMethod.invoke(e);
        return new Attribute<>(label, value);
    }

    @SneakyThrows
    public <E extends Enum<E>> int to(E e) {
        return (Integer) e.getClass().getMethod("getValue").invoke(e);
    }

    @Named("entityType")
    public Attribute<Integer> entityType(int type) {
        return toAttribute(EntityType.get(type));
    }



}
