package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/2 21:42
 */
@Data
public class EntryVO {

    //基础信息
    private int id;
    private Attribute<Integer> type;
    private String name;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;
    private String date;

    private String detail;
    private String remark;
    private String addedTime;
    private String editedTime;
    private boolean status;

}