package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.entity.view.MusicAlbumView;
import com.rakbow.kureakurusu.entity.Music;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-11-06 0:32
 */
@Mapper
public interface MusicMapper extends BaseMapper<Music> {

    Music getMusic(int id, boolean status);

    List<Music> getMusics(List<Integer> ids);

    List<Music> getAll();

    List<Music> getMusicsByAlbumId(long albumId);

    List<Music> getMusicsByAlbumIds(List<Long> ids);

    int getMusicRows();

    int addMusic(Music music);

    void updateMusic(int id, Music music);

    void deleteMusicById(int id);

    void deleteMusicByAlbumId(int albumId);

    void updateMusicArtists(int id, String artists, Timestamp editedTime);

    void updateMusicLyricsText(int id, String lrcText, Timestamp editedTime);

    void updateMusicFiles(int id, List<File> files, Timestamp editedTime);

    //简单搜索
    List<MusicAlbumView> simpleSearch(String keyWorld, int limit, int offset);

    int simpleSearchCount(String keyWorld);

}
