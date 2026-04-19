package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.annotation.Search;
import com.rakbow.kureakurusu.dao.IndexElementMapper;
import com.rakbow.kureakurusu.dao.IndexMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.Index;
import com.rakbow.kureakurusu.data.entity.IndexElement;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.EntryType;
import com.rakbow.kureakurusu.data.enums.ImageType;
import com.rakbow.kureakurusu.data.vo.index.IndexElementItemVO;
import com.rakbow.kureakurusu.data.vo.index.IndexVO;
import com.rakbow.kureakurusu.data.vo.item.ItemSimpleVO;
import com.rakbow.kureakurusu.data.vo.temp.EntitySearchVO;
import com.rakbow.kureakurusu.data.vo.temp.EpisodeSearchVO;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.interceptor.UserContextHolder;
import com.rakbow.kureakurusu.toolkit.CollectionUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.MybatisBatchUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2025/7/24 21:56
 */
@Service
@RequiredArgsConstructor
public class IndexService extends ServiceImpl<IndexMapper, Index> {

    private final EpisodeService epSrv;
    private final MybatisBatchUtil mybatisBatchUtil;
    private final IndexElementMapper indexElementMapper;
    private final IndexMapper mapper;
    private final Converter converter;
    private final ImageService imgSrv;
    private final ResourceService resSrv;
    private final QiniuImageUtil qiniuImageUtil;

    public IndexVO detail(long id) {
        Index idx = getById(id);
        if (idx == null) throw ErrorFactory.entityNotFound();
        IndexVO res = converter.convert(idx, IndexVO.class);
        res.setCover(CommonImageUtil.getEntryCover(idx.getCover()));
        return res;
    }

    public void create(Index idx) {
        idx.setCreatedBy(UserContextHolder.getCurrentUser().getName());
        idx.setCreatedAt(DateHelper.now());
        idx.setUpdatedAt(DateHelper.now());
        save(idx);
    }

    public void update(IndexDTO.IndexUpdateDTO idx) {
        mapper.update(new LambdaUpdateWrapper<>() {{
            eq(Index::getId, idx.id());
            set(Index::getName, idx.name());
            set(Index::getRemark, idx.remark());
            set(Index::getUpdatedAt, DateHelper.now());
        }});
    }

    @Search
    public SearchResult<Index> list(IndexListQueryDTO dto) {
        LoginUser user = UserContextHolder.getCurrentUser();
        IPage<Index> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<>() {{
                    eq(Index::getCreatedBy, user.getName());
                    eq(Index::getType, dto.getType());
                    orderBy(dto.isSort(), dto.asc(), dto.getSortField());
                    orderByDesc(!dto.isSort(), Index::getCreatedAt);
                }}
        );
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    @Search
    public SearchResult<Index> search(IndexListQueryDTO dto) {
        IPage<Index> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<Index>()
                        .eq(Objects.nonNull(dto.getType()), Index::getType, dto.getType())
                        .like(StringUtil.isNotBlank(dto.getKeyword()), Index::getName, dto.getKeyword())
                        .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                        .orderByDesc(!dto.isSort(), Index::getCreatedAt)
        );
        pages.getRecords().forEach(idx -> idx.setCover(CommonImageUtil.getEntryCover(idx.getCover())));
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
    }

    public void addElement(IndexItemCreateDTO dto) {
        List<IndexElement> items = new ArrayList<>();
        for (long itemId : dto.itemIds()) {
            long count = indexElementMapper.selectCount(new LambdaQueryWrapper<IndexElement>()
                    .eq(IndexElement::getIndexId, dto.listId())
                    .eq(IndexElement::getEntityType, dto.type())
                    .eq(IndexElement::getEntityId, itemId));
            if (count > 0) continue;
            items.add(new IndexElement(dto.listId(), dto.type(), itemId));
        }
        if (CollectionUtil.isEmpty(items)) return;
        //batch insert
        mybatisBatchUtil.batchInsert(items, IndexElementMapper.class);
        update(new LambdaUpdateWrapper<Index>().set(Index::getUpdatedAt, DateHelper.now())
                .eq(Index::getId, dto.listId()));
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    @Search
    public SearchResult<IndexElementItemVO> getItems(IndexItemSearchQueryDTO dto) {
        SearchResult<IndexElementItemVO> res;
        if (StringUtil.isNotBlank(dto.getGroupField())) {
            res = getGroupedItem(dto);
        } else {
            res = getSortedItem(dto);
        }
        getLocalResourceCompletedFlag(res.data);
        return res;
    }

    @SneakyThrows
    private SearchResult<IndexElementItemVO> getSortedItem(IndexItemSearchQueryDTO dto) {
        dto.setOffset((dto.getPage() - 1) * dto.getSize());
        List<ItemSimpleVO> items = mapper.getSortedItem(dto);
        if (items.isEmpty()) return new SearchResult<>();
        long total = mapper.countSortedItem(dto);

        List<IndexElementItemVO> elements = converter.convert(items, IndexElementItemVO.class);
        //get image cache
        elements.forEach(i -> {
            i.setCover(imgSrv.getCache(EntityType.ITEM.getValue(), i.getId(), ImageType.MAIN));
            i.setThumb(imgSrv.getCache(EntityType.ITEM.getValue(), i.getId(), ImageType.THUMB));
        });

        return new SearchResult<>(elements, total);
    }

    @SneakyThrows
    private SearchResult<IndexElementItemVO> getGroupedItem(IndexItemSearchQueryDTO dto) {
        dto.setOffset((dto.getPage() - 1) * dto.getSize());
        if (StringUtil.isNotBlank(dto.getGroupField())) {
            switch (dto.getGroupField()) {
                case "product" -> dto.setEntryType(EntryType.PRODUCT.getValue());
                case "character" -> dto.setEntryType(EntryType.CHARACTER.getValue());
                case "classification" -> dto.setEntryType(EntryType.CLASSIFICATION.getValue());
                case "material" -> dto.setEntryType(EntryType.MATERIAL.getValue());
                case "event" -> dto.setEntryType(EntryType.EVENT.getValue());
            }
        }

        List<ItemSimpleVO> items = mapper.getGroupedItem(dto);
        if (items.isEmpty()) return new SearchResult<>();
        long total = mapper.countGroupedItem(dto);

        // Convert to ItemSearchVO and get image cache
        List<IndexElementItemVO> elements = converter.convert(items, IndexElementItemVO.class);
        elements.forEach(i -> {
            i.setCover(imgSrv.getCache(EntityType.ITEM.getValue(), i.getId(), ImageType.MAIN));
            i.setThumb(imgSrv.getCache(EntityType.ITEM.getValue(), i.getId(), ImageType.THUMB));
            i.setEntryThumb(CommonImageUtil.getEntryThumb(i.getEntryThumb()));
        });

        return new SearchResult<>(elements, total);
    }

    private void getLocalResourceCompletedFlag(List<IndexElementItemVO> items) {
        LoginUser user = UserContextHolder.getCurrentUser();
        if (!user.isAdmin()) return;
        resSrv.getLocalResourceCompletedFlag(items);
    }

    //region temp
    @SneakyThrows
    @SuppressWarnings({"unchecked", "rawTypes"})
    @Search
    public SearchResult<?> getElements(IndexItemListQueryDTO dto) {
        int targetEntityType = dto.getType();
        ListQueryDTO param = dto.getParam();
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
        } else {
            throw new ApiException();
        }

        return new SearchResult<>(targets, pages.getTotal());
    }
    //endregion

    public void updateElement(IndexDTO.IndexElementUpdateDTO dto) {
        indexElementMapper.update(new LambdaUpdateWrapper<>() {{
            eq(IndexElement::getId, dto.indexElementId());
            set(IndexElement::getRemark, dto.remark());
        }});
    }

    @SneakyThrows
    @Transactional
    public String uploadImage(long id, MultipartFile file) {
        ImageDTO.ImageMiniDTO image = new ImageDTO.ImageMiniDTO(0, file);
        String cover = qiniuImageUtil.uploadEntryImage(EntityType.INDEX.getValue(), id, image);
        update(null, new LambdaUpdateWrapper<Index>()
                .eq(Index::getId, id)
                .set(Index::getCover, cover)
                .set(Index::getUpdatedAt, DateHelper.now())
        );
        return cover;
    }

}
