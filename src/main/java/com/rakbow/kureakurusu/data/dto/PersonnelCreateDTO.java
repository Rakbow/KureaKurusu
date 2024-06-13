package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/12 11:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonnelCreateDTO extends DTO{

    private int entityType;
    private long entityId;
    private long roleId;
    private List<Long> personIds;

}
