package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 0:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateStatusDTO extends DTO {

    private int entity;
    private List<Long> ids;
    private boolean status;

    public int status() {
        return this.status ? 1 : 0;
    }

}
