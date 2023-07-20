package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.view.MusicAlbumView;
import com.rakbow.kureakurusu.entity.Music;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-06 0:32
 * @Description:
 */
@Mapper
public interface MusicMapper {

    Music getMusic(int id, boolean status);

    List<Music> getMusics(List<Integer> ids);

    List<Music> getAll();

    List<Music> getMusicsByAlbumId(int albumId);

    List<Music> getMusicsByAlbumIds(List<Integer> ids);

    int getMusicRows();

    int addMusic(Music music);

    void updateMusic(int id, Music music);

    void deleteMusicById(int id);

    void deleteMusicByAlbumId(int albumId);

    void updateMusicArtists(int id, String artists, Timestamp editedTime);

    void updateMusicLyricsText(int id, String lrcText, Timestamp editedTime);

    void updateMusicFiles(int id, String files, Timestamp editedTime);

    //简单搜索
    List<MusicAlbumView> simpleSearch(String keyWorld, int limit, int offset);

    int simpleSearchCount(String keyWorld);

}
