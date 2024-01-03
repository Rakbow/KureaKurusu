package com.rakbow.kureakurusu.data.dto;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-04-06 10:34
 */
@Data
public class QueryParams {

    public int page;
    public int size;
    public String sortField;
    public int sortOrder;
    public JSONObject filters;

    public QueryParams() {
        page = 0;
        size = 0;
        sortField = "";
        sortOrder = 0;
        filters = new JSONObject();
    }

    public QueryParams(JSONObject param) {
        JSONObject json = param.getJSONObject("queryParams");
        size = json.getIntValue("rows");
        page = json.getIntValue("first")/size + 1;
        sortField = json.getString("sortField");
        sortOrder = json.getIntValue("sortOrder");
        filters = json.getJSONObject("filters");
    }

    public String getString(String key) {
        return this.filters.getJSONObject(key).getString("value");
    }

    public Boolean getBoolean(String key) {
        return this.filters.getJSONObject(key).getBoolean("value");
    }

    public <T> List<T> getArray(String key, Class<T> clazz) {
        return this.filters.getJSONObject(key).getList("value", clazz);
    }

    public JSONArray getJSONArray(String key) {
        return this.filters.getJSONObject(key).getJSONArray("value");
    }

}
