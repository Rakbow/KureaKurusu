package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/14 4:51
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonDeleteDTO extends DTO {

    private List<Long> ids;

}
