package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 0:29
 */
@Data
public class EntryVO {

    private long id;
    private Attribute<Integer> type;
    private Attribute<Integer> subType;
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;

    private String date;
    private int gender;

    private String cover;
    private String thumb;

    private String detail;
    private String remark;
    private Boolean status;

}
