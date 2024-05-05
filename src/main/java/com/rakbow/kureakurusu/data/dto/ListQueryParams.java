package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2023-04-06 10:34
 */
@Data
public class ListQueryParams {

    public int page;
    public int size;
    public String sortField;
    public int sortOrder;
    public LinkedHashMap<String, LinkedHashMap<String, Object>> filters;

    private static String VALUE_KEY = "value";

    public ListQueryParams() {
        page = 0;
        size = 0;
        sortField = "";
        sortOrder = 0;
        filters = new LinkedHashMap<>();
    }

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
    }

    public ListQueryParams(ListQueryDTO qry) {
        size = qry.getRows();
        page = qry.getFirst()/size + 1;
        sortField = qry.getSortField();
        sortOrder = qry.getSortOrder();
        filters = qry.getFilters();
    }

    public String getStr(String key) {
        Object value = this.filters.get(key).get(VALUE_KEY);
        if(value == null)
            return EMPTY;
        return value.toString();
    }

    public Boolean getBool(String key) {
        Object value = this.filters.get(key).get(VALUE_KEY);
        if(value == null)
            return null;
        return (Boolean) value;
    }

    public Integer getInteger(String key) {
        Object value = this.filters.get(key).get(VALUE_KEY);
        if(value == null)
            return null;
        return (Integer) value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getArray(String key) {
        Object value = this.filters.get(key).get(VALUE_KEY);
        if(value == null)
            return null;
        return (List<T>) value;
    }

}
