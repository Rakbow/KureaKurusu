package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.EntityListVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/5/20 18:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntryListVO extends EntityListVO {

    private Attribute<Integer> type;
    private Attribute<Integer> subType;
    private String date;
    private Attribute<Integer> gender;
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;

    private long visitNum;

}
