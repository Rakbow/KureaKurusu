package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.FavListItemMapper;
import com.rakbow.kureakurusu.dao.FavListMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.FavListItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.FavListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListItemCreateDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.entity.FavList;
import com.rakbow.kureakurusu.data.entity.FavListItem;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.vo.favList.FavListVO;
import com.rakbow.kureakurusu.data.vo.temp.EntitySearchVO;
import com.rakbow.kureakurusu.data.vo.temp.EpisodeSearchVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final FavListMapper mapper;
    private final ItemMapper itemMapper;
    private final EpisodeMapper epMapper;
    private final Converter converter;

    public FavListVO detail(long id) {
        FavList list = getById(id);
        if (list == null) throw ErrorFactory.entityNull();
        return converter.convert(list, FavListVO.class);
    }

    public void create(FavList list) {
        list.setCreator(AuthorityInterceptor.getCurrentUser().getUsername());
        list.setCreateTime(DateHelper.now());
        list.setUpdateTime(DateHelper.now());
        save(list);
    }

    public SearchResult<FavList> lists(FavListQueryDTO dto) {
        User user = AuthorityInterceptor.getCurrentUser();
        IPage<FavList> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<FavList>()
                        .eq(FavList::getCreator, user.getUsername())
                        .eq(FavList::getType, dto.getType())
                        .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                        .orderByDesc(!dto.isSort(), FavList::getCreateTime)
        );
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
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
    @SuppressWarnings({"unchecked", "rawTypes"})
    public SearchResult<? extends EntitySearchVO> getItems(FavListItemListQueryDTO dto) {
        int targetEntityType = dto.getType();
        ListQueryDTO param = dto.getParam();
        param.init();
        IPage<? extends EntitySearchVO> pages;
        Page page = new Page<>(param.getPage(), param.getSize());
        List<? extends EntitySearchVO> targets;
        if (dto.getType() == EntityType.EPISODE.getValue()) {
            pages = mapper.episodes(page, dto.getListId(), param);
        } else {
            throw new Exception("");
        }
        if (pages.getRecords().isEmpty()) return new SearchResult<>();
        targets = pages.getRecords();
        if (targetEntityType == EntityType.EPISODE.getValue()) {
            epSrv.getRelatedParents((List<EpisodeSearchVO>) targets);
        }else {
            throw new Exception("");
        }

        return new SearchResult<>(targets, pages.getTotal());
    }

}
