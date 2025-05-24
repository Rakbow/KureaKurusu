package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/9 22:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemBonusUpdateDTO extends DTO {

    private long id;
    private String bonus;

}
