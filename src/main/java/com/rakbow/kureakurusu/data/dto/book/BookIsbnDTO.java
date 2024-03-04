package com.rakbow.kureakurusu.data.dto.book;

import com.rakbow.kureakurusu.data.dto.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/3/4 14:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookIsbnDTO extends DTO {

    private String label;
    private String isbn;

}
