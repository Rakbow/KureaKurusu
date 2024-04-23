package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.handler.IntegerListHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/8 18:17
 */
@Data
@ToString(callSuper = true)
@TableName(value = "item_album", autoResultMap = true)
public class ItemAlbum {

    private Long id;//表主键
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String catalogNo;//专辑编号
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String barcode;//商品条形码
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> publishFormat;//出版形式 在mysql中以数组字符串形式存储
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//媒体类型
    private double price;//发行价格
    private Currency currency;//货币单位
    private Boolean hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息

    public ItemAlbum() {
        this.id = 0L;
        this.catalogNo = "";
        this.barcode = "";
        this.publishFormat = new ArrayList<>();
        this.albumFormat = new ArrayList<>();
        this.mediaFormat = new ArrayList<>();
        this.price = 0;
        this.currency = Currency.JPY;
        this.hasBonus = false;
        this.bonus = "";
    }

    public ItemAlbum(AlbumUpdateDTO dto) {
        this.id = dto.getId();
        this.catalogNo = dto.getCatalogNo();
        this.barcode = dto.getBarcode();
//        this.publishFormat = dto.getPublishFormat();
//        this.albumFormat = dto.getAlbumFormat();
//        this.mediaFormat = dto.getMediaFormat();
        this.price = dto.getPrice();
        currency = Currency.get(dto.getCurrency());
        hasBonus = dto.isHasBonus();
    }
}
