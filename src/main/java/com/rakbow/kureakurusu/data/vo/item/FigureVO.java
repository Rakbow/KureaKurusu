package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/7/26 17:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FigureVO extends ItemVO {

    private Attribute<Integer> figureType;
    private String scale;

}
