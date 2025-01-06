package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.*;
import com.rakbow.kureakurusu.data.vo.entry.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2025/1/7 0:46
 */
public class EntryUtil {

    private final static Map<Integer, Class<? extends Entry>> subEntryMap = new HashMap<>() {{
        put(EntityType.SUBJECT.getValue(), Subject.class);
        put(EntityType.PERSON.getValue(), Person.class);
        put(EntityType.CHARACTER.getValue(), Chara.class);
        put(EntityType.PRODUCT.getValue(), Product.class);
    }};

    private final static Map<Integer, Class<? extends EntryVO>> entryDetailVOMap = new HashMap<>() {{
        put(EntityType.SUBJECT.getValue(), SubjectVO.class);
        put(EntityType.PERSON.getValue(), PersonVO.class);
        put(EntityType.CHARACTER.getValue(), CharaVO.class);
        put(EntityType.PRODUCT.getValue(), ProductVO.class);
    }};

    public static Class<? extends Entry> getSubClass(Integer type) {
        return subEntryMap.get(type);
    }

    public static Class<? extends EntryVO> getDetailVO(int type) {
        return entryDetailVOMap.get(type);
    }

}
