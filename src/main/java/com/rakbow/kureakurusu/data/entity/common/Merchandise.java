package com.rakbow.kureakurusu.data.entity.common;

import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author Rakbow
 * @since 2023-05-02 16:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Merchandise extends MetaEntry {

    public Merchandise() {
        this.setId(0);
        this.setCategory(EntryCategory.MERCH_TYPE.getAttribute());
        this.setName("");
        this.setNameEn("");
        this.setNameZh("");
        this.setAlias(new ArrayList<>());
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateHelper.getCurrentTime());
        this.setEditedTime(DateHelper.getCurrentTime());
    }

}
