package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.FavListItemMapper;
import com.rakbow.kureakurusu.dao.FavListMapper;
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
import com.rakbow.kureakurusu.exception.EntityNullException;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
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
public class ListService extends ServiceImpl<FavListMapper, FavList> {

    private final EpisodeService epSrv;
    private final MybatisBatchUtil mybatisBatchUtil;
    private final FavListMapper mapper;
    private final Converter converter;

    public FavListVO detail(long id) {
        FavList list = getById(id);
        if (list == null) throw new EntityNullException();
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
        for (long itemId : dto.itemIds()) {
            items.add(new FavListItem(dto.listId(), dto.type(), itemId));
        }
        update(new LambdaUpdateWrapper<FavList>().set(FavList::getUpdateTime, DateHelper.now())
                .eq(FavList::getId, dto.listId()));
        //batch insert
        mybatisBatchUtil.batchInsert(items, FavListItemMapper.class);
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked", "rawTypes"})
    public SearchResult<? extends EntitySearchVO> getItems(FavListItemListQueryDTO dto) {
        int targetEntityType = dto.getType();
        ListQueryDTO param = dto.getParam();
        param.init();
        IPage<? extends EntitySearchVO> pages = new Page<>();
        Page page = new Page<>(param.getPage(), param.getSize());
        List<? extends EntitySearchVO> targets;
        if (dto.getType() == EntityType.EPISODE.getValue()) {
            // pages = mapper.episodes(page, dto.getListId(), param);
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
