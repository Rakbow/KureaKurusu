package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/26 17:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FigureVO extends ItemVO {

    private String scale;
    private List<String> versions;

}
