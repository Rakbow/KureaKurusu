package com.rakbow.kureakurusu.data.vo.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/26 17:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FigureListVO extends ItemListVO {

    private String scale;
    private String various;
    private String title;
    private String titleEn;
    private List<String> versions;
    private List<String> versionsEn;

}
