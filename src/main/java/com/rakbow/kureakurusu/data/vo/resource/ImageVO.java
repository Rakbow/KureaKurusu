package com.rakbow.kureakurusu.data.vo.resource;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/26 20:24
 */
@Data
@AllArgsConstructor
public class ImageVO {

    private Long id;
    private Attribute<Integer> type;
    private String name;
    private String size;
    private String url;
    private String detail;
    private String display;
    private String thumb;
    private String addedTime;
    private String editedTime;

}
