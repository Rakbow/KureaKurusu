package com.rakbow.kureakurusu.data.vo.music;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-14 15:56
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class MusicVO {

    private int id;
    private String name;// 曲名（原文）
    private String nameEn;// 曲名（英语）
    private JSONArray artists;// 创作人员名单 [{"pos":"","name":""}]
    private Attribute audioType;// 音频类型 0-未分类 1-歌曲 2-歌曲（无伴奏） 3-原声 4-广播剧
    private int albumId;// 所属专辑id
    private int discSerial;// 所属碟片的顺序
    private String trackSerial;// 在碟片内的顺序
    private String cover;// 封面图片url
    private JSONArray files;// 歌词文件url
    private String lrcText;// 歌词文本 markdown格式
    private String audioLength;// 音频长度
    private String remark;// 备注

    //其他字段
    private boolean uploadDisabled;

}
