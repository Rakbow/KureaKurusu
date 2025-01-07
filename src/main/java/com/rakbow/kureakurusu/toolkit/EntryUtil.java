package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.entity.entry.*;
import com.rakbow.kureakurusu.data.vo.entry.*;

import java.util.Map;

/**
 * @author Rakbow
 * @since 2025/1/7 0:46
 */
public class EntryUtil {

    private final static Map<Integer, Class<? extends Entry>> subEntryMap = Map.of(
            EntityType.SUBJECT.getValue(), Subject.class,
            EntityType.PERSON.getValue(), Person.class,
            EntityType.CHARACTER.getValue(), Chara.class,
            EntityType.PRODUCT.getValue(), Product.class
    );

    private final static Map<Integer, Class<? extends EntryVO>> entryDetailVOMap = Map.of(
            EntityType.SUBJECT.getValue(), SubjectVO.class,
            EntityType.PERSON.getValue(), PersonVO.class,
            EntityType.CHARACTER.getValue(), CharaVO.class,
            EntityType.PRODUCT.getValue(), ProductVO.class
    );

    private final static Map<Integer, Class<? extends Entry>> entrySearchMap = Map.of(
        EntrySearchType.PERSON.getValue(), Person.class,
        EntrySearchType.CHARACTER.getValue(), Chara.class,
        EntrySearchType.PRODUCT.getValue(), Product.class,
        EntrySearchType.CLASSIFICATION.getValue(), Subject.class,
        EntrySearchType.MATERIAL.getValue(), Subject.class,
        EntrySearchType.EVENT.getValue(), Subject.class
    );

    private final static Map<Integer, Integer> entrySearchEntityMap = Map.of(
            EntrySearchType.PERSON.getValue(), EntityType.PERSON.getValue(),
            EntrySearchType.CHARACTER.getValue(), EntityType.CHARACTER.getValue(),
            EntrySearchType.PRODUCT.getValue(), EntityType.PRODUCT.getValue(),
            EntrySearchType.CLASSIFICATION.getValue(), EntityType.SUBJECT.getValue(),
            EntrySearchType.MATERIAL.getValue(), EntityType.SUBJECT.getValue(),
            EntrySearchType.EVENT.getValue(), EntityType.SUBJECT.getValue()
    );

    public static Class<? extends Entry> getSubClass(Integer type) {
        return subEntryMap.get(type);
    }

    public static Class<? extends EntryVO> getDetailVO(int type) {
        return entryDetailVOMap.get(type);
    }

    public static Class<? extends Entry> getClassByEntrySearchType(Integer type) {
        return entrySearchMap.get(type);
    }

    public static Integer getEntityTypeByEntrySearchType(Integer type) {
        return entrySearchEntityMap.get(type);
    }

}
