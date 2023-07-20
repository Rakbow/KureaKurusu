package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.Album;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-07-19 1:01
 * @Description:
 */
@Mapper
public interface AlbumMapper {

    //根据id查询专辑信息
    Album getAlbum(int id, boolean status);

    List<Album> getAlbums(List<Integer> ids);

    //超详细查询
    List<Album> getAlbumsByFilter(String catalogNo, String name, List<Integer> franchises, List<Integer> products,
                                  List<Integer> publishFormat, List<Integer> albumFormat,
                                  List<Integer> mediaFormat, String hasBonus, boolean status, String sortField, int sortOrder,
                                  int first, int row);

    //超详细查询条数
    int getAlbumRowsByFilter(String catalogNo, String name, List<Integer> franchises, List<Integer> products,
                             List<Integer> publishFormat, List<Integer> albumFormat,
                             List<Integer> mediaFormat, String hasBonus, boolean status);

    //新增专辑
    int addAlbum(Album album);

    //更新专辑信息
    void updateAlbum(int id, Album album);

    //删除单个专辑
    void deleteAlbumById(int id);

    //查询所有专辑信息 offset：每页起始行行号，limit：每页显示数量
    List<Album> getAll();

    //更新专辑图片
    void updateAlbumImages(int id, String images, Timestamp editedTime);

    //更新音轨信息
    void updateAlbumTrackInfo(int id, String trackInfo, Timestamp editedTime);

    //获取最新添加专辑, limit
    List<Album> getAlbumOrderByAddedTime(int limit);

    //简单搜索
    List<Album> simpleSearch(String keyWorld, int limit, int offset);

    int simpleSearchCount(String keyWorld);
}
