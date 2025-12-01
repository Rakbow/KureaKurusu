package com.rakbow.kureakurusu.data.vo.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/5 7:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumListVO extends ItemListVO {

    private int discs;
    private int episodes;
    private int runTime;

}