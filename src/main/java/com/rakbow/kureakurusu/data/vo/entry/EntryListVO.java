package com.rakbow.kureakurusu.data.vo.entry;

import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/5/20 18:23
 */
@Data
public class EntryListVO {

    private long id;
    private int searchType;
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;

    private String remark;
    private String addedTime;
    private String editedTime;
    private boolean status;

    private long visitNum;

}
