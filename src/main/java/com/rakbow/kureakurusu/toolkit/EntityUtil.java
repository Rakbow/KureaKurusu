package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.entity.entry.Chara;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Rakbow
 * @since 2023-02-06 16:38
 */
@RequiredArgsConstructor
@Component
public class EntityUtil {

    private final VisitUtil visitUtil;
    private final LikeUtil likeUtil;
    private final PopularUtil popularUtil;
    private final RedisUtil redisUtil;

    private final static Map<Integer, Class<? extends Entry>> subEntityMap = new HashMap<>() {{
        put(EntityType.PERSON.getValue(), Person.class);
        put(EntityType.PRODUCT.getValue(), Product.class);
        put(EntityType.CHARACTER.getValue(), Chara.class);
        put(EntityType.SUBJECT.getValue(), Subject.class);
    }};

    public Class<? extends Entry> getSubEntity(int type) {
        return subEntityMap.get(type);
    }

    /**
     * 获取页面数据
     *
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public PageTraffic buildTraffic(int entityType, long entityId) {
        // 从cookie中获取点赞token和访问token
        String likeToken = TokenInterceptor.getLikeToken();
        String visitToken = TokenInterceptor.getVisitToken();
        PageTraffic traffic = PageTraffic.builder()
                .liked(likeUtil.isLike(entityType, entityId, likeToken))
                .likeCount(likeUtil.get(entityType, entityId))
                .visitCount(visitUtil.inc(entityType, entityId, visitToken))
                .build();
        //update entity popularity
        popularUtil.updatePopularity(entityType, entityId);
        return traffic;
    }

    public long getEntityTotalCache(EntityType type) {
        String key = String.format(RedisKey.ENTITY_TOTAL_COUNT, type.getValue());
        return Long.parseLong(redisUtil.get(key).toString());
    }
}
