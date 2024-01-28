package com.rakbow.kureakurusu.util.common;

import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.*;

/**
 * @author Rakbow
 * @since 2023-02-17 2:11
 */
@Component
public class VisitUtil {

    @Resource
    private RedisUtil redisUtil;

    public static final String SPLIT = ":";
    public static final String PREFIX_VISIT_TOKEN = "visit_token";
    public static final String PREFIX_VISIT = "visit";

    /**
     * 新增浏览数存入redis缓存
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public void addVisit(int entityType, long entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        redisUtil.set(key, 1);
    }

    /**
     * 获取浏览数
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public long getVisit(int entityType, long entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        if(!redisUtil.hasKey(key)) {
            addVisit(entityType, entityId);
        }
        return Integer.parseInt(redisUtil.get(key).toString());
    }

    /**
     * 自增并返回浏览数
     * @param entityType,entityId 实体类型,实体id
     * @author Rakbow
     */
    public long incVisit(int entityType, long entityId, String visitToken) {
        String key = getSingleVisitKey(entityType, entityId);
        String tokenKey = getEntityVisitTokenKey(entityType, entityId, visitToken);
        if(redisUtil.hasKey(tokenKey)) {
            return getVisit(entityType, entityId);
        }else {
            redisUtil.set(tokenKey, 1);
            redisUtil.expire(tokenKey, 3600*24);
            return redisUtil.increment(key, 1);
        }
    }

    /**
     * 删除浏览数
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public void deleteVisit(int entityType, long entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        redisUtil.delete(key);
    }

    /**
     * 获取实体访问token key,用于判断是否第一次访问
     * */
    public String getEntityVisitTokenKey(int entityType, long entityId, String visitToken) {
        return PREFIX_VISIT_TOKEN + SPLIT + entityType + SPLIT + entityId + SPLIT + visitToken;
    }

    /**
     * 获取实体浏览数key,用于记录浏览数
     * */
    public String getSingleVisitKey(int entityType, long entityId) {
        return PREFIX_VISIT + SPLIT + entityType + SPLIT + entityId;
    }




    /**
     * 获取浏览数排名,并返回指定数量的排名数据
     * @param entityType 实体类型
     * @author Rakbow
     */
    public LinkedHashMap<Integer, Long> getEntityVisitRanking(int entityType, int limit) {
        LinkedHashMap<Integer, Long> res = new LinkedHashMap<>();
        //rankKey
        String rankKey = getEntityVisitRankingKeyName(entityType);
        if(redisUtil.hasKey(rankKey)) {
            //0,-1的参数代表查询该key下的所有value
            Set<Object> keys = redisUtil.redisTemplate.opsForZSet().reverseRange(rankKey, 0, limit);
            for (Object key : keys) {
                res.put(Integer.parseInt(key.toString()), Math.round(redisUtil.redisTemplate.opsForZSet().score(rankKey, key)));
            }
        }
        return res;
    }

    /**
     * 清空所有浏览数据
     * @author Rakbow
     */
    public void clearAllVisitRank() {

        if(redisUtil.hasKey(RedisKey.ALBUM_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.ALBUM_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.BOOK_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.BOOK_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.DISC_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.DISC_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.GAME_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.GAME_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.MERCH_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.MERCH_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.MUSIC_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.MUSIC_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.PRODUCT_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.PRODUCT_VISIT_RANKING, 0, -1);
        }
        if(redisUtil.hasKey(RedisKey.FRANCHISE_VISIT_RANKING)) {
            redisUtil.redisTemplate.opsForZSet().removeRange(RedisKey.FRANCHISE_VISIT_RANKING, 0, -1);
        }

    }

    /**
     * 更新浏览数排名的set
     * @param entityType,entityId,visitCount 实体类型,实体id,浏览数
     * @author Rakbow
     */
    public void setEntityVisitRanking(int entityType, int entityId, long visitCount) {
        String key = String.valueOf(entityId);
        String rankingKey = getEntityVisitRankingKeyName(entityType);
        redisUtil.redisTemplate.opsForZSet().add(rankingKey, key, visitCount);
    }

    public String getEntityVisitRankingKeyName(int entityType) {

        if(entityType == Entity.ALBUM.getValue()) {
            return RedisKey.ALBUM_VISIT_RANKING;
        }
        if(entityType == Entity.BOOK.getValue()) {
            return RedisKey.BOOK_VISIT_RANKING;
        }
        if(entityType == Entity.DISC.getValue()) {
            return RedisKey.DISC_VISIT_RANKING;
        }
        if(entityType == Entity.GAME.getValue()) {
            return RedisKey.GAME_VISIT_RANKING;
        }
//        if(entityType == Entity.MERCH.getId()) {
//            return RedisCacheConstant.MERCH_VISIT_RANKING;
//        }
        if(entityType == Entity.EPISODE.getValue()) {
            return RedisKey.MUSIC_VISIT_RANKING;
        }
//        if(entityType == Entity.PRODUCT.getId()) {
//            return RedisCacheConstant.PRODUCT_VISIT_RANKING;
//        }
//        if(entityType == Entity.FRANCHISE.getId()) {
//            return RedisCacheConstant.FRANCHISE_VISIT_RANKING;
//        }
        return "";
    }



}
