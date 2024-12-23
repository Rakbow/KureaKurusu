package com.rakbow.kureakurusu.data.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.emun.ReleaseType;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/26 2:34
 */
@Data
public abstract class SuperItem {

    private Long id;
    private ItemType type;

    private String name;
    private String nameEn;
    private List<String> aliases;

    private String releaseDate;
    @AutoMapping(qualifiedByName = "toAttribute")
    private ReleaseType releaseType;
    private double price;
    @AutoMapping(target = "currency", qualifiedByName = "getCurrency")
    private String region;
    private String barcode;
    private String CatalogId;

    private double width;// mm
    private double length;// mm
    private double height;// mm
    private double weight;// g

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
        nameEn = "";
        aliases = new ArrayList<>();
        releaseDate = "-";
        releaseType = ReleaseType.STANDARD;
        region = "jp";
        barcode = "";
        CatalogId = "";

        width = 0;
        length = 0;
        height = 0;
        weight = 0;

        bonus = true;
        detail = "";
        remark = "";
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = true;
    }

}
