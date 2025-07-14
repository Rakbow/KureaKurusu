package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/6/9 23:38
 */
@Data
@Builder
@AllArgsConstructor
public class EntryMiniVO {

    private Long id;
    private Attribute<Integer> type;
    private Attribute<Integer> subType;
    private String name;
    private String subName;
    private String thumb;
    private Attribute<Integer> gender;
    private String date;

    public EntryMiniVO(Entry entry) {
        this.id = entry.getId();
        this.type = new Attribute<>(entry.getType());
        this.subType = new Attribute<>(entry.getSubType());
        this.name = entry.getName();
        this.subName = CommonUtil.getSubName(entry.getNameEn(), entry.getNameZh());
        this.thumb = CommonImageUtil.getEntryThumb(entry.getThumb());
        this.date = entry.getDate();
        this.gender = new Attribute<>(entry.getGender());
    }

}
