package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.EntityStatistic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-22 18:58
 * @Description:
 */
@Mapper
public interface StatisticMapper {

    EntityStatistic getStatistic(int entityType, int entityId);

    List<EntityStatistic> getAll();

    void addStatistic(EntityStatistic entityStatistic);

    //更新浏览数
    void updateVisitCount(int entityType, int entityId, long visitCount);

    //更新点赞数
    void updateLikeCount(int entityType, int entityId, long visitCount);

    //更新收藏数
    void updateCollectCount(int entityType, int entityId, long visitCount);

    void deleteStatistic(int entityType, int entityId);

    List<EntityStatistic> getPopularItems(int entityType);

}
