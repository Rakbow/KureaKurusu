package com.rakbow.kureakurusu.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-13 22:59
 * @Description:
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
