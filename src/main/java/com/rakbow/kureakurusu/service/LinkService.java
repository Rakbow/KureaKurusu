package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.LinkMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Link;
import com.rakbow.kureakurusu.data.enums.LinkType;
import com.rakbow.kureakurusu.data.vo.LinkVO;
import com.rakbow.kureakurusu.data.vo.LinksVO;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2025/12/7 20:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    private final Converter converter;
    private final RedisUtil redisUtil;

    @SuppressWarnings("unchecked")
    @Transactional
    public List<LinksVO> group(int entityType, long entityId) {
        String key = STR."entity_links:\{entityType}:\{entityId}";
        if (!redisUtil.hasKey(key)) {
            refreshLinks(entityType, entityId);
        }log.info(key);
        List<LinksVO> res = (List<LinksVO>) redisUtil.get(key);
        res.forEach(lnk -> lnk.setType(new Attribute<>(LinkType.get(lnk.getType().getValue()))));
        return res;
    }

    @Transactional
    public void refreshLinks(int entityType, long entityId) {
        String key = STR."entity_links:\{entityType}:\{entityId}";
        List<LinksVO> res = new ArrayList<>();
        List<Link> links = list(new LambdaUpdateWrapper<Link>()
                .eq(Link::getEntityType, entityType).eq(Link::getEntityId, entityId)
                .orderByAsc(Link::getType, Link::getTag));
        if (links.isEmpty()) {
            redisUtil.set(key, res);
            return;
        }
        List<LinkVO> vos = converter.convert(links, LinkVO.class);
        res = vos.stream()
                .collect(Collectors.groupingBy(LinkVO::getType))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getKey().getValue()))
                .map(entry -> new LinksVO(entry.getKey(), entry.getValue()))
                .toList();
        log.info(JsonUtil.toJson(res));
        redisUtil.set(key, res);

    }

}
