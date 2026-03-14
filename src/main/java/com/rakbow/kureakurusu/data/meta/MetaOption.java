package com.rakbow.kureakurusu.data.meta;

import com.rakbow.kureakurusu.data.Attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2023-11-05 4:49
 */
public class MetaOption {

    public Map<Long, Attribute<Long>> roleSet;

    //album
    public List<Attribute<Integer>> albumFormatSet;
    public List<Attribute<Integer>> mediaFormatSet;

    public MetaOption() {
        roleSet = new HashMap<>();
        albumFormatSet = new ArrayList<>();
        mediaFormatSet = new ArrayList<>();
    }

}
