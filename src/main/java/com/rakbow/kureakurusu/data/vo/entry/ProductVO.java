package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-13 9:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductVO extends EntryVO {

    private Attribute<Integer> type;
    private String date;

}
