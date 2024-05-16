package com.rakbow.kureakurusu.toolkit;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2023-02-18 19:41
 */
@Component
@RequiredArgsConstructor
public class LikeUtil {

    private final RedisUtil redisUtil;
    private static final String PREFIX_LIKE_TOKEN = "like_token";
    private static final String PREFIX_LIKE = "like";

    /**
     * 获取点赞数
     * */
    public long get(int entityType, long entityId) {
        String key = getEntityLikeKey(entityType, entityId);
        if(!redisUtil.hasKey(key)) return 0;
        return Long.parseLong(redisUtil.get(key).toString());
    }

    /**
     * 点赞自增
     * */
    public void inc(int entityType, long entityId, String likeToken) {
        String key = getEntityLikeKey(entityType, entityId);
        String tokenKey = getEntityLikeTmpTokenKey(entityType, entityId, likeToken);
        if(redisUtil.hasKey(key)) {
            redisUtil.increment(key, 1);
        }else {
            add(entityType, entityId);
        }
        redisUtil.set(tokenKey, 1, 3600*24);
    }

    /**
     * 新增点赞
     * */
    public void add(int entityType, long entityId) {
        String key = getEntityLikeKey(entityType, entityId);
        redisUtil.set(key, 1);
    }

    /**
     * 删除点赞
     * */
    public void del(int entityType, long entityId) {
        String key = getEntityLikeKey(entityType, entityId);
        redisUtil.delete(key);
    }

    /**
     * 获取实体点赞key,用于记录点赞数
     * */
    public String getEntityLikeKey(int entityType, long entityId) {
        return STR."\{PREFIX_LIKE}:\{entityType}:\{entityId}";
    }

    /**
     * 获取实体点赞token key,用于判断是否点过赞
     * */
    public String getEntityLikeTmpTokenKey(int entityType, long entityId, String likeToken) {
        return STR."\{PREFIX_LIKE_TOKEN}:\{entityType}:\{entityId}:\{likeToken}";
    }

    /**
     * 判断是否点过赞
     * */
    public boolean isLike(int entityType, long entityId, String likeToken) {
        if(StringUtils.isBlank(likeToken)) return false;
        String key = getEntityLikeTmpTokenKey(entityType, entityId, likeToken);
        return redisUtil.hasKey(key);
    }



}
