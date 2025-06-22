package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListQuery extends QueryDTO {

    private int first;
    private int rows;
    private String sortField;
    private int sortOrder;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> filters;

    private static String VALUE_KEY = "value";

    @SuppressWarnings("unchecked")
    public <T> T getVal(String key) {
        Object value = null;
        if(this.filters.containsKey(key))
            value = this.filters.get(key).get(VALUE_KEY);
        if(value == null || value.toString().isEmpty())
            return null;
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getArray(String key) {
        Object value = null;
        if(this.filters.containsKey(key))
            value = this.filters.get(key).get(VALUE_KEY);
        if(value == null)
            return null;
        return (List<T>) value;
    }

    public boolean isPage() {
        return this.getRows() != 0;
    }

}