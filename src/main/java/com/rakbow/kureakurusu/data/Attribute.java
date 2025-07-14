package com.rakbow.kureakurusu.data;

import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * @author Rakbow
 * @since 2023-04-30 18:40
 */
@Data
public class Attribute<T> {

    private String label;
    private T value;

    public Attribute() {
        this.label = "";
        this.value = null;
    }

    public Attribute(String label, T value) {
        this.label = label;
        this.value = value;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <E extends Enum<E>> Attribute(E e) {
        Method getValueMethod = e.getClass().getMethod("getValue");
        Method getLabelMethod = e.getClass().getMethod("getLabelKey");
        this.label = I18nHelper.getMessage(getLabelMethod.invoke(e).toString());
        this.value = (T) getValueMethod.invoke(e);
    }

}
