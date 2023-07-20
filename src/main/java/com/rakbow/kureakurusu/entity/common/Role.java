package com.rakbow.kureakurusu.entity.common;

import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.util.common.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-02 16:39
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Role extends MetaEntry {

    public Role() {
        this.setId(0);
        this.setCategory(EntryCategory.ROLE.getAttribute());
        this.setName("");
        this.setNameEn("");
        this.setNameZh("");
        this.setAlias(new ArrayList<>());
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateUtil.getCurrentTime());
        this.setEditedTime(DateUtil.getCurrentTime());
    }

}
