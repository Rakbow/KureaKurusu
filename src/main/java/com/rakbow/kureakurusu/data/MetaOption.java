package com.rakbow.kureakurusu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 4:49
 * @Description:
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
