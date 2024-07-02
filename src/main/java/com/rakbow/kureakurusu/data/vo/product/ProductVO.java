package com.rakbow.kureakurusu.data.vo.product;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-13 9:42
 */
@Data
public class ProductVO {

    //基础信息
    private int id;
    private Attribute<Integer> type;
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private String date;

    private String detail;
    private String remark;
    private String addedTime;
    private String editedTime;
    private boolean status;

}
