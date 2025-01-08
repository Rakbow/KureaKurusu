package com.rakbow.kureakurusu.toolkit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2025/1/8 8:05
 */
@Component
@RequiredArgsConstructor
public class PopularUtil {

    private final RedisUtil redisUtil;
    private final VisitUtil visitUtil;
    private final LikeUtil likeUtil;

    private static final String POPULAR_RANK = "entity_popular_rank";


    public void updatePopularity(int entityType, long entityId) {
        String key = STR."\{POPULAR_RANK}:\{entityType}";
        long visit = visitUtil.get(entityType, entityId);
        long like = likeUtil.get(entityType, entityId);
        double popularity = EntityPopularCalculator.calculateHotness(visit, like, System.currentTimeMillis());
        redisUtil.updateZSet(key, entityId, popularity);
    }

    public Set<Long> getPopularityRank(int entityType, int topN) {
        String key = STR."\{POPULAR_RANK}:\{entityType}";

        Set<Object> zset = redisUtil.getZSet(key, topN);

        if(zset.isEmpty()) return new HashSet<>() {};

        return zset.stream()
                .map(obj -> {
                    // 确保 obj 转换为 Long
                    if (obj instanceof Number) {
                        return ((Number) obj).longValue();
                    } else if (obj instanceof String) {
                        return Long.parseLong((String) obj);
                    } else {
                        throw new IllegalArgumentException(STR."不能转换为 Long: \{obj}");
                    }
                })
                .collect(Collectors.toSet());
    }

}
