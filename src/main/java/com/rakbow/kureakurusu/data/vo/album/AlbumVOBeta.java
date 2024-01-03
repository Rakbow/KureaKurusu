package com.rakbow.kureakurusu.data.vo.album;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * VO 信息量最少
 *
 * @author Rakbow
 * @since 2023-01-11 16:13
 */
@Data
public class AlbumVOBeta {

    //基础信息
    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String releaseDate;//发行日期

    private JSONObject cover;//封面

    private List<Attribute<Integer>> albumFormat;//专辑分类
    private String addedTime;//收录时间
    private String editedTime;//编辑时间

}
