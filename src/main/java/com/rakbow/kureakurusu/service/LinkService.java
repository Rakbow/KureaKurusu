package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.LinkMapper;
import com.rakbow.kureakurusu.data.entity.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/12/7 20:50
 */
@Service
@RequiredArgsConstructor
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    public List<Link> list(int entityType, long entityId) {
        return list(new LambdaUpdateWrapper<Link>()
                .eq(Link::getEntityType, entityType).eq(Link::getEntityId, entityId)
                .orderByAsc(Link::getId));
    }

}
