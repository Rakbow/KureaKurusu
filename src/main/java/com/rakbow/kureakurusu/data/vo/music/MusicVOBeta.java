package com.rakbow.kureakurusu.data.vo.music;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-04-12 16:53
 * @Description: VO 存储到搜索引擎数据库
 */
@Data
public class MusicVOBeta {

    //基础信息
    private int id;//主键编号
    private String name;//曲名（原文）
    private String artists;//
    private int albumId;//所属专辑id
    private String albumName;//所属专辑名
    private String audioType;//音频类型
    private String cover;// 封面图片url
    private String audioLength;// 音频长度
    private boolean hasLrc;//是否有歌词
    private boolean hasFile;//是否有附件

    private long visitCount;//浏览数
    private long likeCount;//点赞数

}
