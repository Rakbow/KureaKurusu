package com.rakbow.kureakurusu.data.entity.item;

import com.rakbow.kureakurusu.data.enums.ItemSubType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.data.enums.ReleaseType;
import com.rakbow.kureakurusu.data.entity.Entity;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/26 2:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SuperItem extends Entity {

    private Long id = 0L;
    private ItemType type;
    private ItemSubType subType;

    private String name;
    private String nameEn;
    private List<String> aliases;

    private String releaseDate;
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

    public SuperItem() {
        super();
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
    }

}
