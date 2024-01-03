package com.rakbow.kureakurusu.data.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-12-13 22:59
 */
@Data
public class Personnel {

    private Attribute<Long> role;
    private List<Attribute<Long>> persons;

    public Personnel() {
        role = new Attribute<>();
        persons = new ArrayList<>();
    }
}
