package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.entity.Game;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-06 14:43
 */
@Mapper
public interface GameMapper extends BaseMapper<Game> {

    //通过id查询Game
    Game getGame(int id, boolean status);

    List<Game> getGames(List<Integer> ids);

    List<Game> getAll();

    //根据过滤条件搜索Game
    List<Game> getGamesByFilter(String name, String hasBonus, List<Integer> franchises, List<Integer> products, int platform,
                                String region, boolean status, String sortField, int sortOrder, int first, int row);

    //超详细查询条数
    int getGamesRowsByFilter(String name, String hasBonus, List<Integer> franchises, List<Integer> products, int platform,
                             String region, boolean status);

    //新增Game
    int addGame (Game game);

    //更新Game基础信息
    int updateGame (int id, Game game);

    //删除单个Game
    int deleteGame(int id);

    //更新相关组织
    int updateGameOrganizations(int id, String organizations, Timestamp editedTime);

    //更新开发制作人员
    int updateGameStaffs(int id, String staffs, Timestamp editedTime);

    //获取最新添加Game, limit
    List<Game> getGamesOrderByAddedTime(int limit);

    //简单搜索
    List<Game> simpleSearch(String keyWorld, int limit, int offset);

    int simpleSearchCount(String keyWorld);

}
