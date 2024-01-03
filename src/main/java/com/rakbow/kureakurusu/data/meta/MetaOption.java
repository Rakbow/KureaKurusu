package com.rakbow.kureakurusu.data.meta;

import com.rakbow.kureakurusu.data.Attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-11-05 4:49
 */
public class MetaOption {

    //性别
    public List<Attribute<Integer>> genderSet;
    public List<Attribute<Integer>> linkTypeSet;
    public List<Attribute<Long>> roleSet;

    public MetaOption() {
        genderSet = new ArrayList<>();
        linkTypeSet = new ArrayList<>();
        roleSet = new ArrayList<>();
    }

}
