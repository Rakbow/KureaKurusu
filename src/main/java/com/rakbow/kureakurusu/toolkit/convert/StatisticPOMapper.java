package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.common.Like;
import com.rakbow.kureakurusu.data.common.Visit;
import com.rakbow.kureakurusu.toolkit.LikeUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.SpringUtil;
import com.rakbow.kureakurusu.toolkit.VisitUtil;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-02-22 20:08
 */
@Mapper(componentModel = "spring", uses = MetaVOMapper.class)
public interface StatisticPOMapper {

    StatisticPOMapper INSTANCES = Mappers.getMapper(StatisticPOMapper.class);

    /**
     * redis_visit_key转PO对象
     *
     * @param visitKey 专辑
     * @return Visit
     * @author rakbow
     */
    default Visit key2Visit(String visitKey) {
        if (StringUtils.isBlank(visitKey)) {
            return null;
        }

        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");

        Visit visit = new Visit();

        String[] s = visitKey.split(VisitUtil.SPLIT);
        visit.setEntityType(Integer.parseInt(s[1]));
        visit.setEntityId(Integer.parseInt(s[2]));
        visit.setVisitCount(Long.parseLong(redisUtil.get(visitKey).toString()));

        return visit;
    }

    /**
     * 列表转换
     *
     * @param visitKeys 列表
     * @return List<Visit>
     * @author rakbow
     */
    default List<Visit> keys2Visit(List<String> visitKeys) {
        List<Visit> visits = new ArrayList<>();

        if (!visitKeys.isEmpty()) {
            visitKeys.forEach(key -> {
                visits.add(key2Visit(key));
            });
        }

        return visits;
    }

    /**
     * redis_like_key转PO对象
     *
     * @param likeKey 专辑
     * @return Like
     * @author rakbow
     */
    default Like key2Like(String likeKey) {
        if (StringUtils.isBlank(likeKey)) {
            return null;
        }

        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");

        Like like = new Like();

        String[] s = likeKey.split(LikeUtil.SPLIT);
        like.setEntityType(Integer.parseInt(s[1]));
        like.setEntityId(Integer.parseInt(s[2]));
        like.setLikeCount(Long.parseLong(redisUtil.get(likeKey).toString()));

        return like;
    }

    /**
     * 列表转换
     *
     * @param likeKeys 列表
     * @return List<Like>
     * @author rakbow
     */
    default List<Like> keys2Like(List<String> likeKeys) {
        List<Like> likes = new ArrayList<>();

        if (!likeKeys.isEmpty()) {
            likeKeys.forEach(key -> {
                likes.add(key2Like(key));
            });
        }

        return likes;
    }

}
