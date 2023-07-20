package com.rakbow.kureakurusu.entity.view;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-04-14 15:14
 * @Description: 视图
 * select music.id, music.name, album.id as album_id, album.name as album_name,
 *  music.audio_type, music.audio_length, music.artists, album.images as album_images,
 *  (json_length(music.files) != 0) as has_file, (music.lrc_text is not null) as has_lrc
 *  from music inner join album on music.album_id = album.id where music.status = 1
 */
@Data
public class MusicAlbumView {

    private int id;//主键
    private String name;//曲名(原)
    private String artists;//作者信息 json数组
    private int albumId;//专辑id
    private String albumName;//专辑名(原)
    private int audioType;//音频类型
    private String albumImages;//专辑图片 json数组
    private String audioLength;//音频长度
    private int hasFile;//附件
    private int hasLrc;//歌词

}
