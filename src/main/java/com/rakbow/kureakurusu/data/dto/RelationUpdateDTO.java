package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/22 17:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationUpdateDTO extends DTO {

    private long id;
    private String remark;

}