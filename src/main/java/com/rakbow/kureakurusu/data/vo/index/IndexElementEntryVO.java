package com.rakbow.kureakurusu.data.vo.index;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2026/4/3 23:30
 */
@Data
public class IndexElementEntryVO {

    private long id;
    private String name;
    private String subName;
    private Attribute<Integer> type;
    private Attribute<Integer> subType;
    private String thumb;
    private String startDate;
    private String endDate;

    private List<IndexElementItemVO> items;

}
