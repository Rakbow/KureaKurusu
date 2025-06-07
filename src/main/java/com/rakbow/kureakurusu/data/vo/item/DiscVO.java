package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/6 18:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiscVO extends ItemVO {

    private int discs;
    private int episodes;
    private int runTime;

    private List<Attribute<Integer>> mediaFormat;

}
