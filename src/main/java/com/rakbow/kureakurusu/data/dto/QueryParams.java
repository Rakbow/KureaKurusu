package com.rakbow.kureakurusu.data.dto;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-04-06 10:34
 * @Description:
 */
@Data
public class QueryParams {

    private int first;
    private int rows;
    private String sortField;
    private int sortOrder;
    private JSONObject filters;

    public QueryParams() {
        first = 0;
        rows = 0;
        sortField = "";
        sortOrder = 0;
        filters = new JSONObject();
    }

}
