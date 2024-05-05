package com.rakbow.kureakurusu.toolkit.converter;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.util.common.DateHelper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/5 8:12
 */
@Component
public class ItemToItemListVO {

    public String timestampToString(Timestamp ts) {
        return DateHelper.timestampToString(ts);
    }

    public <T> List<Attribute<T>> toAttributes(List<T> list) {
        List<Attribute<T>> res = new ArrayList<>();



        return res;
    }

}