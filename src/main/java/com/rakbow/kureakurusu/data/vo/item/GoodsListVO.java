package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/7/26 17:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsListVO extends ItemListVO {

    private Attribute<Integer> goodsType;
    private String scale;

}
