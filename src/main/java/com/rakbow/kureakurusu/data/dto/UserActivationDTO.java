package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/3/13 15:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserActivationDTO extends DTO {

    private long userId;
    private String code;

}
