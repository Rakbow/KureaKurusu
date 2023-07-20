package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.entity.Disc;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-27 19:43
 * @Description:
 */
@Mapper
public interface DiscMapper {

    //通过id查询disc
    Disc getDisc(int id, boolean status);

    List<Disc> getDiscs(List<Integer> ids);

    List<Disc> getAll();

    //根据过滤条件搜索disc
    List<Disc> getDiscsByFilter(String catalogNo, String name, String region, List<Integer> franchises, List<Integer> products,
                                List<Integer> mediaFormat, String limited, String hasBonus, boolean status, String sortField,
                                int sortOrder, int first, int row);

    //超详细查询条数
    int getDiscsRowsByFilter(String catalogNo, String name, String region, List<Integer> franchises, List<Integer> products,
                             List<Integer> mediaFormat, String limited, String hasBonus, boolean status);

    //新增disc
    int addDisc (Disc disc);

    //更新disc基础信息
    void updateDisc (int id, Disc disc);

    //删除单个disc
    void deleteDisc(int id);

    //获取最新添加Disc, limit
    List<Disc> getDiscsOrderByAddedTime(int limit);

    //简单搜索
    List<Disc> simpleSearch(String keyWorld, int limit, int offset);

    int simpleSearchCount(String keyWorld);

}
