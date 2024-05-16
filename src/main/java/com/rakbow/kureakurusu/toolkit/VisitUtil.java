package com.rakbow.kureakurusu.toolkit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2023-02-17 2:11
 */
@Component
@RequiredArgsConstructor
public class VisitUtil {

    private final RedisUtil redisUtil;
    private static final String PREFIX_VISIT_TOKEN = "visit_token";
    private static final String PREFIX_VISIT = "visit";

    /**
     * 新增浏览数存入redis缓存
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public void add(int entityType, long entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        redisUtil.set(key, 1);
    }

    /**
     * 获取浏览数
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public long get(int entityType, long entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        if(!redisUtil.hasKey(key)) add(entityType, entityId);
        return Long.parseLong(redisUtil.get(key).toString());
    }

    /**
     * 自增并返回浏览数
     * @param entityType,entityId 实体类型,实体id
     * @author Rakbow
     */
    public long inc(int entityType, long entityId, String visitToken) {
        String key = getSingleVisitKey(entityType, entityId);
        String tokenKey = getEntityVisitTokenKey(entityType, entityId, visitToken);
        if(redisUtil.hasKey(tokenKey)) {
            return get(entityType, entityId);
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
    public void del(int entityType, long entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        redisUtil.delete(key);
    }

    /**
     * 获取实体访问token key,用于判断是否第一次访问
     * */
    private String getEntityVisitTokenKey(int entityType, long entityId, String visitToken) {
        return STR."\{PREFIX_VISIT_TOKEN}:\{entityType}:\{entityId}:\{visitToken}";
    }

    /**
     * 获取实体浏览数key,用于记录浏览数
     * */
    private String getSingleVisitKey(int entityType, long entityId) {
        return STR."\{PREFIX_VISIT}:\{entityType}:\{entityId}";
    }

}
