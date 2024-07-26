package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/7/26 17:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsVO extends ItemVO {

    private Attribute<Integer> goodsType;
    private String scale;

}