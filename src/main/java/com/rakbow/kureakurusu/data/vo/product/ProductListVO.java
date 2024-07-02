package com.rakbow.kureakurusu.data.vo.product;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * 转换量较少的VO，一般用于list index页面
 *
 * @author Rakbow
 * @since 2023-01-13 9:42
 */
@Data
public class ProductListVO {

    private int id;
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private String date;//发售日期
    private Attribute<Integer> type;//作品分类
    private String remark;

    //审计字段
    private String addedTime;
    private String editedTime;
    private boolean status;

}
