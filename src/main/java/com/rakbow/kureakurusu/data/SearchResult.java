package com.rakbow.kureakurusu.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-07 18:26
 */
@AllArgsConstructor
public class SearchResult<T> {

    public long total;//查询结果数
    public List<T> data;//查询结果数据
    public long page;
    public long pageSize;
    public String searchTime;

    public SearchResult(List<T> data, long total) {
        this.total = total;
        this.data = data;
        this.searchTime = DateHelper.nowStr();
    }

    public SearchResult(List<T> data, long total, long page, long pageSize) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.searchTime = DateHelper.nowStr();
    }

    public SearchResult(IPage<T> page) {
        this.total = page.getTotal();
        this.data = page.getRecords();
        this.page = page.getCurrent();
        this.pageSize = page.getSize();
        this.searchTime = DateHelper.nowStr();
    }

    public SearchResult() {
        total = 0;
        data = null;
        page = 0;
        pageSize = 10;
        searchTime = DateHelper.nowStr();
    }
}
