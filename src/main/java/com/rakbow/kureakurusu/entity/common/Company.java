package com.rakbow.kureakurusu.entity.common;

import com.rakbow.kureakurusu.data.emun.common.Region;
import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import com.rakbow.kureakurusu.util.common.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-04-29 17:29
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Company extends MetaEntry {

    private List<String> links;//链接 json数组
    private RegionVO region;//国家或地区编码

    public Company() {
        this.setId(0);
        this.setCategory(EntryCategory.COMPANY.getAttribute());
        this.setName("");
        this.setNameEn("");
        this.setNameZh("");
        this.setAlias(new ArrayList<>());
        this.setLinks(new ArrayList<>());
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateUtil.getCurrentTime());
        this.setEditedTime(DateUtil.getCurrentTime());

        this.region = Region.getRegion(Region.GLOBAL.getCode());
    }

}
