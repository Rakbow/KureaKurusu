package com.rakbow.kureakurusu.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-30 17:24
 * @Description:
 */
@Data
public class SimpleSearchParam {

    private String keyword;
    private long page;
    private long size;

    public SimpleSearchParam() {
        this.keyword = "";
        this.page = 1;
        this.size = 10;
    }

    public SimpleSearchParam(String keyword, int first, int row) {
        this.keyword = keyword;
        this.page = first/row + 1;
        this.size = row;
    }

    public SimpleSearchParam(JSONObject json) {
        this.keyword = json.getString("keyword");
        this.size = json.getIntValue("row");
        this.page = json.getIntValue("first")/size + 1;
    }
}
