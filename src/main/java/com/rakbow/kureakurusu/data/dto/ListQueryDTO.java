package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.toolkit.CommonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

/**
 * @author Rakbow
 * @since 2025/5/23 19:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListQueryDTO extends QueryDTO {

    private int page;
    private int size;
    private String sortField;
    private int sortOrder;
    private String keyword;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> filters = new LinkedHashMap<>();

    private static String VALUE_KEY = "value";

    public ListQueryDTO() {
        size = isPage() ? getSize() : -1;
        keyword = getVal("keyword");
    }

    public void init() {}

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
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
