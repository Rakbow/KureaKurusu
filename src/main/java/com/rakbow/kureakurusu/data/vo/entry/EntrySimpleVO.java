package com.rakbow.kureakurusu.data.vo.entry;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.emun.EntrySubType;
import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.emun.Gender;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/14 21:22
 */
@Data
public class EntrySimpleVO {

    private Long id = 0L;
    private EntryType type;
    private EntrySubType subType;
    private String name;

    private String nameZh;
    private String nameEn;

    private Gender gender;
    private String date;

    private String thumb;

    @TableField(exist = false)
    private int items;

}
