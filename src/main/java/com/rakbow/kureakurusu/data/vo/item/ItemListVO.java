package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.vo.ImageVO;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/5 7:50
 */
@Data
public class ItemListVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private String ean13;
    private String releaseDate;
    private double price;
    private String currency;
    private String remark;

    private ImageVO cover;

    private String addedTime;
    private String editedTime;
    private boolean status;

    private long visitNum;

}