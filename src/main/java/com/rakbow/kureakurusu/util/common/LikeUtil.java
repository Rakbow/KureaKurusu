package com.rakbow.kureakurusu.util.common;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2023-02-18 19:41
 */
@Component
public class LikeUtil {

    @Resource
    private RedisUtil redisUtil;

    public static final String SPLIT = ":";
    public static final String PREFIX_LIKE_TOKEN = "like_token";
    public static final String PREFIX_LIKE = "like";

    /**
     * 获取点赞数
     * */
    public long getLike(int entityType, long entityId) {
        String key = getEntityLikeKey(entityType, entityId);
        if(redisUtil.hasKey(key)) {
            return Long.parseLong(redisUtil.get(key).toString());
        }else {
            return 0;
        }

    }

    /**
     * 点赞自增
     * */
    public void incLike(int entityType, long entityId, String likeToken) {
        String key = getEntityLikeKey(entityType, entityId);
        String tokenKey = getEntityLikeTmpTokenKey(entityType, entityId, likeToken);
        if(redisUtil.hasKey(key)) {
            redisUtil.increment(key, 1);
        }else {
            addLike(entityType, entityId);
        }
        redisUtil.set(tokenKey, 1);
        redisUtil.expire(tokenKey, 3600*24);
    }

    /**
     * 新增点赞
     * */
    public void addLike(int entityType, long entityId) {
        String key = getEntityLikeKey(entityType, entityId);
        redisUtil.set(key, 1);
    }

    /**
     * 删除点赞
     * */
    public void deleteLike(int entityType, long entityId) {
        String key = getEntityLikeKey(entityType, entityId);
        redisUtil.delete(key);
    }

    /**
     * 获取实体点赞key,用于记录点赞数
     * */
    public String getEntityLikeKey(int entityType, long entityId) {
        return PREFIX_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 获取实体点赞token key,用于判断是否点过赞
     * */
    public String getEntityLikeTmpTokenKey(int entityType, long entityId, String likeToken) {
        return PREFIX_LIKE_TOKEN + SPLIT + entityType + SPLIT + entityId + SPLIT + likeToken;
    }

    /**
     * 判断是否点过赞
     * */
    public boolean isLike(int entityType, long entityId, String likeToken) {
        if(StringUtils.isBlank(likeToken))
            return false;
        return redisUtil.hasKey(getEntityLikeTmpTokenKey(entityType, entityId, likeToken));
    }



}
