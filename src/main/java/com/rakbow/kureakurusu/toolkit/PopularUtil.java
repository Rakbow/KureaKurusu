package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.EntryType;
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
    private final HotnessCalculator hotnessCalculator;


    public void updateEntityPopularity(int entityType, long id) {
        String key = null;
        if(entityType == EntityType.ITEM.getValue()) {
            key = RedisKey.ITEM_POPULAR_RANK;
        }else if (entityType == EntityType.EPISODE.getValue()) {
            key = RedisKey.EPISODE_POPULAR_RANK;
        }
        long visit = visitUtil.get(entityType, id);
        long like = likeUtil.get(entityType, id);
        double popularity = hotnessCalculator.calculateItemHotness(visit, like, System.currentTimeMillis());
        redisUtil.updateZSet(key, id, popularity);
    }

    public void updateEntryPopularity(EntryType type, long id) {
        String key = null;
        switch (type) {
            case EntryType.PRODUCT -> key = RedisKey.PRODUCT_POPULAR_RANK;
            case EntryType.PERSON -> key = RedisKey.PERSON_POPULAR_RANK;
            case EntryType.CHARACTER -> key = RedisKey.CHARACTER_POPULAR_RANK;
            case EntryType.CLASSIFICATION -> key = RedisKey.CLASSIFICATION_POPULAR_RANK;
            case EntryType.MATERIAL -> key = RedisKey.MATERIAL_POPULAR_RANK;
            case EntryType.EVENT -> key = RedisKey.EVENT_POPULAR_RANK;
        }
        long visit = visitUtil.get(type.getValue(), id);
        long like = likeUtil.get(type.getValue(), id);
        double popularity = hotnessCalculator.calculateItemHotness(visit, like, System.currentTimeMillis());
        redisUtil.updateZSet(key, id, popularity);
    }

    public Set<Long> getEntryPopularityRank(int type, int topN) {
        String key = null;
        if(type == EntryType.PRODUCT.getValue()) {
            key = RedisKey.PRODUCT_POPULAR_RANK;
        }else if (type == EntryType.PERSON.getValue()) {
            key = RedisKey.PERSON_POPULAR_RANK;
        }else if (type == EntryType.CHARACTER.getValue()) {
            key = RedisKey.CHARACTER_POPULAR_RANK;
        }else if (type == EntryType.CLASSIFICATION.getValue()) {
            key = RedisKey.CLASSIFICATION_POPULAR_RANK;
        }else if (type == EntryType.MATERIAL.getValue()) {
            key = RedisKey.MATERIAL_POPULAR_RANK;
        }else if (type == EntryType.EVENT.getValue()) {
            key = RedisKey.EVENT_POPULAR_RANK;
        }
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
