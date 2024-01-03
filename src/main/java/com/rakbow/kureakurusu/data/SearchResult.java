package com.rakbow.kureakurusu.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.AllArgsConstructor;

/**
 * @author Rakbow
 * @since 2023-01-07 18:26
 */
@AllArgsConstructor
public class SearchResult {

    public long total;//查询结果数
    public Object data;//查询结果数据
    public long page;
    public long pageSize;
    public String searchTime;

    public SearchResult(Object data, long total) {
        this.total = total;
        this.data = data;
        this.searchTime = DateHelper.timestampToString(DateHelper.NOW_TIMESTAMP);
    }

    public <T> SearchResult(Object data, IPage<T> page) {
        this.data = data;
        this.total = page.getTotal();
        this.page = page.getCurrent();
        this.pageSize = page.getSize();
        this.searchTime = DateHelper.timestampToString(DateHelper.NOW_TIMESTAMP);
    }

    public <T> SearchResult(IPage<T> page) {
        this.total = page.getTotal();
        this.data = page.getRecords();
        this.page = page.getCurrent();
        this.pageSize = page.getSize();
        this.searchTime = DateHelper.timestampToString(DateHelper.NOW_TIMESTAMP);
    }

    public SearchResult() {
        total = 0;
        data = null;
        page = 0;
        pageSize = 10;
        searchTime = DateHelper.timestampToString(DateHelper.NOW_TIMESTAMP);
    }
}
