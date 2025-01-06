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
    public List<Attribute<Integer>> relatedGroupSet;

    //album
    public List<Attribute<Integer>> albumFormatSet;
    public List<Attribute<Integer>> mediaFormatSet;
    public List<Attribute<Integer>> productTypeSet;
    public List<Attribute<Integer>> subjectTypeSet;
    public List<Attribute<Integer>> entityTypeSet;
    public List<Attribute<Integer>> bookTypeSet;
    public List<Attribute<String>> currencySet;
    public List<Attribute<String>> languageSet;
    public List<Attribute<Integer>> releaseTypeSet;
    public List<Attribute<Integer>> goodsTypeSet;
    public List<Attribute<Integer>> figureTypeSet;
    public List<Attribute<Integer>> imageTypeSet;

    public MetaOption() {
        genderSet = new ArrayList<>();
        linkTypeSet = new ArrayList<>();
        roleSet = new ArrayList<>();

        albumFormatSet = new ArrayList<>();
        mediaFormatSet = new ArrayList<>();
        productTypeSet = new ArrayList<>();
        entityTypeSet = new ArrayList<>();
        subjectTypeSet = new ArrayList<>();

        bookTypeSet = new ArrayList<>();
        currencySet = new ArrayList<>();
        languageSet = new ArrayList<>();
        releaseTypeSet = new ArrayList<>();
        goodsTypeSet = new ArrayList<>();
        figureTypeSet = new ArrayList<>();
    }

}
