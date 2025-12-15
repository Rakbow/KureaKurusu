package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.LinkMapper;
import com.rakbow.kureakurusu.data.entity.Link;
import com.rakbow.kureakurusu.data.vo.LinkVO;
import com.rakbow.kureakurusu.data.vo.LinksVO;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2025/12/7 20:50
 */
@Service
@RequiredArgsConstructor
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    private final Converter converter;

    public List<LinksVO> group(int entityType, long entityId) {
        List<Link> list = list(new LambdaUpdateWrapper<Link>()
                .eq(Link::getEntityType, entityType).eq(Link::getEntityId, entityId)
                .orderByAsc(Link::getType, Link::getTag));
        List<LinkVO> vos = converter.convert(list, LinkVO.class);
        return vos.stream()
                .collect(Collectors.groupingBy(LinkVO::getType))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(
                        e -> e.getKey().getValue()
                ))
                .map(entry -> new LinksVO(entry.getKey(), entry.getValue()))
                .toList();
    }

}
