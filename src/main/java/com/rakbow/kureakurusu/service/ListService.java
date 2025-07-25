package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.FavListItemMapper;
import com.rakbow.kureakurusu.dao.FavListMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.FavListItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.FavListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListItemCreateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.vo.favList.FavListItemTargetVO;
import com.rakbow.kureakurusu.data.vo.favList.FavListItemVO;
import com.rakbow.kureakurusu.data.vo.favList.FavListVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/24 21:56
 */
@Service
@RequiredArgsConstructor
public class ListService extends ServiceImpl<FavListMapper, FavList> {

    private final ImageService imgSrv;
    private final EpisodeService epSrv;
    private final SqlSessionFactory sqlSessionFactory;
    private final FavListItemMapper iMapper;
    private final ItemMapper itemMapper;
    private final EpisodeMapper epMapper;
    private final Converter converter;

    public FavListVO detail(long id) {
        FavList list = getById(id);
        if(list == null) throw  ErrorFactory.entityNull();
        return converter.convert(list, FavListVO.class);
    }

    public void create(FavList list) {
        list.setCreator(AuthorityInterceptor.getCurrentUser().getUsername());
        list.setCreateTime(DateHelper.now());
        list.setUpdateTime(DateHelper.now());
        save(list);
    }

    public SearchResult<FavList> lists(FavListQueryDTO dto) {
        dto.init();
        long start = System.currentTimeMillis();
        User user = AuthorityInterceptor.getCurrentUser();
        IPage<FavList> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<FavList>()
                        .eq(FavList::getCreator, user.getUsername())
                        .eq(FavList::getType, dto.getType())
                        .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                        .orderByDesc(!dto.isSort(), FavList::getCreateTime)
        );
        return new SearchResult<>(pages.getRecords(), pages.getTotal(), start);
    }

    public void addItems(ListItemCreateDTO dto) {
        List<FavListItem> items = new ArrayList<>();
        for (long itemId : dto.getItemIds()) {
            items.add(new FavListItem(dto.getListId(), dto.getType(), itemId));
        }
        update(new LambdaUpdateWrapper<FavList>().set(FavList::getUpdateTime, DateHelper.now())
                .eq(FavList::getId, dto.getListId()));
        //batch insert
        MybatisBatch.Method<FavListItem> method = new MybatisBatch.Method<>(FavListItemMapper.class);
        MybatisBatch<FavListItem> batchInsert = new MybatisBatch<>(sqlSessionFactory, items);
        batchInsert.execute(method.insert());
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public SearchResult<FavListItemVO> getItems(FavListItemListQueryDTO dto) {
        dto.init();
        long start = System.currentTimeMillis();
        IPage<FavListItem> page = iMapper.selectPage(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<FavListItem>().eq(FavListItem::getListId, dto.getListId())
        );
        if (page.getRecords().isEmpty()) return new SearchResult<>();

        List<FavListItemVO> res = new ArrayList<>();
        List<FavListItem> items = page.getRecords();
        List<Long> targetIds = items.stream().map(FavListItem::getEntityId).toList();
        BaseMapper<? extends Entity> subMapper;
        int targetEntityType = dto.getType();
        if (targetEntityType == EntityType.ITEM.getValue()) {
            subMapper = itemMapper;
        } else if (targetEntityType == EntityType.EPISODE.getValue()) {
            subMapper = epMapper;
        } else {
            throw new Exception();
        }
        List<? extends Entity> targets = subMapper.selectByIds(targetIds);
        if(targetEntityType == EntityType.EPISODE.getValue()) {
            epSrv.getRelatedAlbums((List<Episode>) targets);
        }
        for (FavListItem i : items) {
            Entity e = DataFinder.findEntityById(i.getEntityId(), targets);
            if (ObjectUtils.isEmpty(e)) continue;

            FavListItemTargetVO target = FavListItemTargetVO.builder()
                    .entityType(targetEntityType)
                    .entityId(e.getId())
                    .name(e.getName())
                    .build();

            if (targetEntityType == EntityType.ITEM.getValue()) {
                target.setThumb(imgSrv.getCache(targetEntityType, e.getId(), ImageType.THUMB));
                target.setSubType(new Attribute<>(((Item) e).getSubType()));
            } else if (targetEntityType == EntityType.ENTRY.getValue()) {
                target.setThumb(CommonImageUtil.getEntryThumb(((Entry) e).getThumb()));
                target.setSubType(new Attribute<>(((Entry) e).getSubType()));
            }else {
                target.setSubInfo(DateHelper.getDuration(((Episode) e).getDuration()));
            }

            FavListItemVO vo = FavListItemVO.builder()
                    .id(i.getId())
                    .target(target)
                    .remark(i.getRemark())
                    .build();

            if(targetEntityType == EntityType.EPISODE.getValue()) {
                assert e instanceof Episode;
                vo.setParent(((Episode) e).getParent());
            }

            res.add(vo);
        }

        return new SearchResult<>(res, page.getTotal(), start);
    }

}
