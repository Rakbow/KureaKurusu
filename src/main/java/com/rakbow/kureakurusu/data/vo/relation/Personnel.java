package com.rakbow.kureakurusu.data.vo.relation;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/2 19:33
 */
@Data
public class Personnel {

    private Attribute<Long> role;
    private List<PersonVO> persons;

}
