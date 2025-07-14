package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2023-02-06 16:38
 */
@RequiredArgsConstructor
@Component
public class EntityUtil {

    private final VisitUtil visitUtil;
    private final LikeUtil likeUtil;
    private final RedisUtil redisUtil;

    /**
     * 获取页面数据
     *
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public PageTraffic buildTraffic(int entityType, long entityId) {
        // 从cookie中获取点赞token和访问token
        String likeToken = TokenInterceptor.getLikeToken();
        return PageTraffic.builder()
                .liked(likeUtil.isLike(entityType, entityId, likeToken))
                .likeCount(likeUtil.get(entityType, entityId))
                .visitCount(visitUtil.get(entityType, entityId))
                .build();
    }

    public long getEntityTotalCache(EntityType type) {
        String key = String.format(RedisKey.ENTITY_TOTAL_COUNT, type.getValue());
        return Long.parseLong(redisUtil.get(key).toString());
    }
}
