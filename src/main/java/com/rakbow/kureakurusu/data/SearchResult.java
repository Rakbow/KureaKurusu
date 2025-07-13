package com.rakbow.kureakurusu.data;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-07 18:26
 */
@AllArgsConstructor
public class SearchResult<T> {

    public List<T> data;
    public long total;
    public String time;

    public SearchResult(List<T> data, long total, long start) {
        this.total = total;
        this.data = data;
        this.time = String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0);
    }

    public SearchResult(List<T> data, long total) {
        this.total = total;
        this.data = data;
    }

    public SearchResult() {
        total = 0;
        data = new ArrayList<>();
        time = "0";
    }
}
