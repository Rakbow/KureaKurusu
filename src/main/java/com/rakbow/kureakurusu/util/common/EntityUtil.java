package com.rakbow.kureakurusu.util.common;

import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.meta.MetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2023-02-06 16:38
 */
@RequiredArgsConstructor
@Component
public class EntityUtil {

    private final RedisUtil redisUtil;
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
                .likeCount(likeUtil.getLike(entityType, entityId))
                .visitCount(visitUtil.incVisit(entityType, entityId, visitToken))
                .build();
    }

    /**
     * 获取页面选项数据
     * @param entityType,entityId,addedTime,editedTime 实体类型，实体id,收录时间,编辑时间
     * @author Rakbow
     */
    public List<Object> getDetailOptions(int entityType) {

        List<Object> options = new ArrayList<>();

        if(entityType == Entity.ALBUM.getValue()) {
            options.add(Objects.requireNonNull(MetaData.getOptions()).albumFormatSet);
            options.add(Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet);
            options.add(Objects.requireNonNull(MetaData.getOptions()).publishFormatSet);
        }
        else if(entityType == Entity.PRODUCT.getValue()) {
            options.add(Objects.requireNonNull(MetaData.getOptions()).franchiseSet);
            options.add(Objects.requireNonNull(MetaData.getOptions()).productCategorySet);
        }

        return options;
    }
}
