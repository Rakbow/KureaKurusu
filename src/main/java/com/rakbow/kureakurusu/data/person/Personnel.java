package com.rakbow.kureakurusu.data.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-12-13 22:59
 */
@Data
@Builder
public class Personnel {

    private long id;//person relation id
    private Attribute<Long> role;
    private Attribute<Long> person;

}
