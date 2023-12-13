package com.rakbow.kureakurusu.entity.common;

import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-02 4:19
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TmpPersonnel extends MetaEntry {

    private List<String> links;//链接 json数组

    public TmpPersonnel() {
        this.setId(0);
        this.setCategory(EntryCategory.PERSONNEL.getAttribute());
        this.setName("");
        this.setNameEn("");
        this.setNameZh("");
        this.setAlias(new ArrayList<>());
        this.setLinks(new ArrayList<>());
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateHelper.getCurrentTime());
        this.setEditedTime(DateHelper.getCurrentTime());
    }

}
