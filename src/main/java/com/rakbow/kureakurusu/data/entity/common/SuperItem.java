package com.rakbow.kureakurusu.data.entity.common;

import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.emun.ReleaseType;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2024/4/26 2:34
 */
@Data
public abstract class SuperItem {

    private Long id;
    private ItemType type;

    private String name;
    private String nameZh;
    private String nameEn;

    private String releaseDate;
    @AutoMapping(qualifiedByName = "toAttribute")
    private ReleaseType releaseType;
    private double price;
    @AutoMapping(target = "currency", qualifiedByName = "getCurrency")
    private String region;
    private String barcode;

    private Boolean bonus;
    private String detail;
    private String remark;
    @AutoMapping(qualifiedByName = "getVOTime")
    private Timestamp addedTime;
    @AutoMapping(qualifiedByName = "getVOTime")
    private Timestamp editedTime;
    private Boolean status;

    public SuperItem() {
        id = 0L;
        name = "";
        nameZh = "";
        nameEn = "";
        releaseDate = "-";
        releaseType = ReleaseType.STANDARD;
        region = "jp";
        barcode = "";
        bonus = true;
        detail = "";
        remark = "";
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = true;
    }

}
