package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static <S, T, K> void matchAndAssign(
            List<S> sourceList,
            List<T> targetList,
            Predicate<S> sourceFilter,           // 过滤 source
            Function<S, K> sourceKey,
            Function<T, K> targetKey,
            BiConsumer<S, T> assigner
    ) {

        if (sourceList == null || targetList == null) {
            return;
        }

        // 1️⃣ 过滤 + 建 Map
        Map<K, S> sourceMap = sourceList.stream()
                .filter(sourceFilter)
                .collect(Collectors.toMap(
                        sourceKey,
                        s -> s,
                        (a, b) -> a
                ));

        // 2️⃣ 匹配赋值
        for (T target : targetList) {
            K key = targetKey.apply(target);
            S source = sourceMap.get(key);
            if (source != null) {
                assigner.accept(source, target);
            }
        }
    }
}
