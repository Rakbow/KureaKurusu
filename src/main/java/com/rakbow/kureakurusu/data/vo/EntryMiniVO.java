package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
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

    private Integer type;// entity type
    private Long id;// entity id
    private String name;// entity name
    private String subName;
    private String thumb;
    private String info;

    public EntryMiniVO(Entry entry) {
        this.type = entry.getType().getValue();
        this.id = entry.getId();
        this.name = entry.getName();
        this.subName = CommonUtil.getSubName(entry.getNameEn(), entry.getNameZh());
        this.thumb = CommonImageUtil.getEntryThumb(entry.getThumb());
    }

}
