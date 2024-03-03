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
    public List<Attribute<Long>> franchiseSet;

    //album
    public List<Attribute<Integer>> albumFormatSet;
    public List<Attribute<Integer>> publishFormatSet;
    public List<Attribute<Integer>> mediaFormatSet;
    public List<Attribute<Integer>> productCategorySet;
    public List<Attribute<Integer>> relationTypeSet;
    public List<Attribute<Integer>> entityTypeSet;
    public List<Attribute<Integer>> bookTypeSet;
    public List<Attribute<String>> currencySet;
    public List<Attribute<String>> languageSet;
    public List<Attribute<String>> regionSet;

    public MetaOption() {
        genderSet = new ArrayList<>();
        linkTypeSet = new ArrayList<>();
        roleSet = new ArrayList<>();
        franchiseSet = new ArrayList<>();

        albumFormatSet = new ArrayList<>();
        publishFormatSet = new ArrayList<>();
        mediaFormatSet = new ArrayList<>();
        productCategorySet = new ArrayList<>();
        relationTypeSet = new ArrayList<>();
        entityTypeSet = new ArrayList<>();

        bookTypeSet = new ArrayList<>();
        currencySet = new ArrayList<>();
        languageSet = new ArrayList<>();
        regionSet = new ArrayList<>();
    }

}
