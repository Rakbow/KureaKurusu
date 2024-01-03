package com.rakbow.kureakurusu.data;

import com.alibaba.fastjson2.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-02-03 18:26
 */
@Data
public class MeiliSearchResult {

    public int total;//查询结果数
    public JSONArray data;//查询结果数据
    public String keyword;//查询关键字
    public int entityType;//查询实体类型
    public int offset;//开始行数
    public int limit;//限制行数

    public MeiliSearchResult() {
        total = 0;
        data = new JSONArray();
        keyword = "";
        entityType = 0;
        offset = 0;
        limit = 0;
    }
}
