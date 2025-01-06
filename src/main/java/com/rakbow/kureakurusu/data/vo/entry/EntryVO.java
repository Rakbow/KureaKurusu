package com.rakbow.kureakurusu.data.vo.entry;

import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 0:29
 */
@Data
public class EntryVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;
    private String detail;
    private String remark;
    private String addedTime;
    private String editedTime;
    private Boolean status;

}
