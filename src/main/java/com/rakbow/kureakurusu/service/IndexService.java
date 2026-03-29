package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.annotation.Search;
import com.rakbow.kureakurusu.dao.IndexItemMapper;
import com.rakbow.kureakurusu.dao.IndexMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.data.dto.IndexItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.IndexListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListItemCreateDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.entity.Index;
import com.rakbow.kureakurusu.data.entity.IndexItem;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.vo.index.IndexVO;
import com.rakbow.kureakurusu.data.vo.temp.EntitySearchVO;
import com.rakbow.kureakurusu.data.vo.temp.EpisodeSearchVO;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.interceptor.UserContextHolder;
import com.rakbow.kureakurusu.toolkit.CollectionUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.MybatisBatchUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/24 21:56
 */
@Service
@RequiredArgsConstructor
public class IndexService extends ServiceImpl<IndexMapper, Index> {

    private final EpisodeService epSrv;
    private final MybatisBatchUtil mybatisBatchUtil;
    private final IndexItemMapper indexItemMapper;
    private final Converter converter;

    public IndexVO detail(long id) {
        Index idx = getById(id);
        if (idx == null) throw ErrorFactory.entityNotFound();
        return converter.convert(idx, IndexVO.class);
    }

    public void create(Index idx) {
        idx.setCreatedBy(UserContextHolder.getCurrentUser().getName());
        idx.setCreatedAt(DateHelper.now());
        idx.setUpdatedAt(DateHelper.now());
        save(idx);
    }

    @Search
    public SearchResult<Index> list(IndexListQueryDTO dto) {
        LoginUser user = UserContextHolder.getCurrentUser();
        IPage<Index> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<Index>()
                        .eq(Index::getCreatedBy, user.getName())
                        .eq(Index::getType, dto.getType())
                        .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                        .orderByDesc(!dto.isSort(), Index::getCreatedAt)
        );
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
    }

    public void addItems(ListItemCreateDTO dto) {
        List<IndexItem> items = new ArrayList<>();
        for (long itemId : dto.itemIds()) {
            long count = indexItemMapper.selectCount(new LambdaQueryWrapper<IndexItem>()
                    .eq(IndexItem::getListId, dto.listId())
                    .eq(IndexItem::getEntityType, dto.type())
                    .eq(IndexItem::getEntityId, itemId));
            if (count > 0) continue;
            items.add(new IndexItem(dto.listId(), dto.type(), itemId));
        }
        if (CollectionUtil.isEmpty(items)) return;
        //batch insert
        mybatisBatchUtil.batchInsert(items, IndexItemMapper.class);
        update(new LambdaUpdateWrapper<Index>().set(Index::getUpdatedAt, DateHelper.now())
                .eq(Index::getId, dto.listId()));
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked", "rawTypes"})
    @Search
    public SearchResult<? extends EntitySearchVO> getItems(IndexItemListQueryDTO dto) {
        int targetEntityType = dto.getType();
        ListQueryDTO param = dto.getParam();
        param.init();
        IPage<? extends EntitySearchVO> pages = new Page<>();
        // Page page = new Page<>(param.getPage(), param.getSize());
        List<? extends EntitySearchVO> targets;
        // if (dto.getType() == EntityType.EPISODE.getValue()) {
        //     // pages = mapper.episodes(page, dto.getListId(), param);
        // } else {
        //     throw new ApiException();
        // }
        if (pages.getRecords().isEmpty()) return new SearchResult<>();
        targets = pages.getRecords();
        if (targetEntityType == EntityType.EPISODE.getValue()) {
            epSrv.getRelatedParents((List<EpisodeSearchVO>) targets);
        }else {
            throw new ApiException();
        }

        return new SearchResult<>(targets, pages.getTotal());
    }

}
