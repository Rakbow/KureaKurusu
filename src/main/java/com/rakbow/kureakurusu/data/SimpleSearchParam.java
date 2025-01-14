package com.rakbow.kureakurusu.data;

import com.rakbow.kureakurusu.data.dto.SearchQry;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-30 17:24
 */
@Data
public class SimpleSearchParam {

    private boolean strict;
    private String keyword;
    private List<String> keywords;
    private long page;
    private long size;

    public boolean keywordEmpty() {
        return StringUtils.isBlank(keyword);
    }

    public boolean strict() {
        return strict;
    }

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
        this.strict = qry.getStrict() == 1;
        this.keyword = qry.getKeyword();
        this.keywords = qry.getKeywords();
        this.size = qry.getRow();
        this.page = qry.getFirst()/size + 1;
    }
}
