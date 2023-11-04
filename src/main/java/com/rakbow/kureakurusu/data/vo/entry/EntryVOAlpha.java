package com.rakbow.kureakurusu.data.vo.entry;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-03 1:53
 * @Description:
 */
@Data
public class EntryVOAlpha {

    //基础信息
    private int id;//表主键
    private String name;//名称
    private String nameZh;//名称(中文)
    private String nameEn;//名称(英语)
    private Attribute<Integer> category;//分类
    private List<String> alias;//别名
    private JSONObject detail;
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private String remark;//备注
    private int status;//状态

}
