package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.handler.FileHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/08 14:54
 */
@Data
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "episode", autoResultMap = true)
public class Episode {

    @Builder.Default
    private Long id = 0L;//表主键
    private String title;//标题(原)
    private String titleEn;//标题(英)
    private String premiereDate;//首播日期
    private int duration;//时长 单位：秒
    private int discNum;//碟片序号
    private int serial;//序号
    @TableField(typeHandler = FileHandler.class)
    @Builder.Default
    private List<File> files = new ArrayList<>();//文件
    private String detail;//详情
    private int episodeType;//类型 0-音乐 1-剧集
    private long relatedId;//关联id 音乐 album_id

    @Builder.Default
    private Timestamp addedTime = DateHelper.now();
    @Builder.Default
    private Timestamp editedTime = DateHelper.now();
    @Builder.Default
    private int status = 1;

    public Episode() {
        id = 0L;
        title = "";
        titleEn = "";
        premiereDate = "";
        duration = 0;
        discNum = 1;
        serial = 1;
        files = new ArrayList<>();
        detail = "";
        episodeType = 0;
        relatedId = 0;
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = 1;
    }

}
