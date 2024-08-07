package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/5 7:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumListVO extends ItemListVO {

    private int discs;
    private int tracks;
    private int runTime;
    private List<Attribute<Integer>> albumFormat;
    private List<Attribute<Integer>> mediaFormat;

}