package com.rakbow.kureakurusu.data;

import com.rakbow.kureakurusu.data.dto.base.SearchQry;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-10-30 17:24
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

    public SimpleSearchParam(SearchQry qry) {
        this.keyword = qry.getKeyword();
        this.size = qry.getRow();
        this.page = qry.getFirst()/size + 1;
    }
}
