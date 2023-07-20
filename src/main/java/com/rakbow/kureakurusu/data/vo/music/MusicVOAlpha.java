package com.rakbow.kureakurusu.data.vo.music;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-14 15:58
 * @Description: 转换量较少的VO，一般用于list index页面
 */
@Data
public class MusicVOAlpha {

    private int id;
    private String name;// 曲名（原文）
    private String nameEn;// 曲名（英语）
    private int discSerial;// 所属碟片的顺序
    private String trackSerial;// 在碟片内的顺序
    private String cover;// 封面图片url
    private String audioLength;// 音频长度

    //审计字段
    private String addedTime;// 收录时间
    private String editedTime;// 编辑时间

}
