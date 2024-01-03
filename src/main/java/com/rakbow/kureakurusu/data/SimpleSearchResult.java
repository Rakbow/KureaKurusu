package com.rakbow.kureakurusu.data;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-02-25 4:08
 */
@Data
public class SimpleSearchResult {

    public long total;//查询结果数
    public JSONArray data;//查询结果数据
    public String keyword;//查询关键字
    public int entityType;//查询实体类型
    public String entityName;//查询实体类型
    public int offset;//开始行数
    public int limit;//限制行数
    public String searchTime;//查询时间

    public SimpleSearchResult(String keyword, int entityType, String entityName, int offset, int limit) {
        this.total = 0;
        this.data = new JSONArray();
        this.keyword = keyword;
        this.entityType = entityType;
        this.entityName = entityName;
        this.offset = offset;
        this.limit = limit;
        this.searchTime = DateHelper.timestampToString(DateHelper.NOW_TIMESTAMP);
    }
}