package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/23 14:45
 */
@Data
public class ChangelogVO {

    private Attribute<Integer> field;
    private Attribute<Integer> operate;
    private String operator;
    private String operateTime;

}
