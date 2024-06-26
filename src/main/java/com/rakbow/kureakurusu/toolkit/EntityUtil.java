package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.meta.MetaData;
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

    /**
     * 获取页面数据
     *
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public PageTraffic getPageTraffic(int entityType, long entityId) {
        // 从cookie中获取点赞token和访问token
        String likeToken = TokenInterceptor.getLikeToken();
        String visitToken = TokenInterceptor.getVisitToken();
        return PageTraffic.builder()
                .liked(likeUtil.isLike(entityType, entityId, likeToken))
                .likeCount(likeUtil.get(entityType, entityId))
                .visitCount(visitUtil.inc(entityType, entityId, visitToken))
                .build();
    }

    /**
     * 获取页面选项数据
     *
     * @param entityType,entityId,addedTime,editedTime 实体类型，实体id,收录时间,编辑时间
     * @author Rakbow
     */
    public Map<String, Object> getDetailOptions(int entityType) {

        Map<String, Object> options = new HashMap<>();

        if (entityType == EntityType.PRODUCT.getValue()) {
            options.put("franchiseSet", Objects.requireNonNull(MetaData.getOptions()).franchiseSet);
            options.put("productCategorySet", Objects.requireNonNull(MetaData.getOptions()).productCategorySet);
        } else if (entityType == EntityType.PERSON.getValue()) {
            options.put("genderSet", Objects.requireNonNull(MetaData.getOptions()).genderSet);
            options.put("linkTypeSet", Objects.requireNonNull(MetaData.getOptions()).linkTypeSet);
        } else if (entityType == -1) {
            options.put("roleSet", Objects.requireNonNull(MetaData.getOptions()).roleSet);
            options.put("entityTypeSet", Objects.requireNonNull(MetaData.getOptions()).entityTypeSet);
            options.put("relationTypeSet", Objects.requireNonNull(MetaData.getOptions()).relationTypeSet);
        }

        return options;
    }
}
