package com.rakbow.kureakurusu.data.vo.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumVO extends ItemVO {

    private int discs;
    private int tracks;
    private int runTime;

}
