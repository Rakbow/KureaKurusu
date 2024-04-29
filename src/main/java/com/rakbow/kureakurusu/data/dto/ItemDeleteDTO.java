package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/29 10:50
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDeleteDTO extends DTO {

    private List<Long> ids;

}