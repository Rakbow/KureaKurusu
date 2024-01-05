package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Music;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2022-11-06 0:32
 */
@Mapper
public interface MusicMapper extends BaseMapper<Music> {

    // void updateMusicFiles(int id, List<File> files, Timestamp editedTime);

    //简单搜索
    // List<MusicAlbumView> simpleSearch(String keyWorld, int limit, int offset);

    // int simpleSearchCount(String keyWorld);

}
