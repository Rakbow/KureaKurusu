package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/12/13 2:19
 */
@Data
public class LinkVO {

    private Attribute<Integer> type;
    private String title;
    private String tag;
    private String url;

}
