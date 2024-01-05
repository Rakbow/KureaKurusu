package com.rakbow.kureakurusu.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.rakbow.kureakurusu.data.image.Image;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-04-14 15:14
 */
@Data
@TableName(value = "music_album_view", autoResultMap = true)
public class MusicAlbumView {

    private int id;//主键
    private String name;//曲名(原)
    private String artists;//作者信息 json数组
    private int albumId;//专辑id
    private String albumName;//专辑名(原)
    private int audioType;//音频类型
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Image> albumImages;//专辑图片 json数组
    private String audioLength;//音频长度
    private int hasFile;//附件
    private int hasLrc;//歌词

}
