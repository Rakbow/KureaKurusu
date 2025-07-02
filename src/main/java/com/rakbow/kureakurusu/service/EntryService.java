package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.RelatedGroup;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.GroupCacheEntryItem;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.EntryMiniVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryListVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.PopularUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2025/1/7 0:49
 */
@Service
@RequiredArgsConstructor
public class EntryService extends ServiceImpl<EntryMapper, Entry> {

    private final Converter converter;
    private final EntityUtil entityUtil;
    private final PopularUtil popularUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final static int ENTITY_TYPE = EntityType.ENTRY.getValue();

    @SneakyThrows
    @Transactional
    public EntryDetailVO detail(long id) {

        Entry entry = getById(id);
        if (entry == null) throw ErrorFactory.entryNull();
        //update popular
        // popularUtil.updateEntryPopularity(entry.getType(), id);
        return EntryDetailVO.builder()
                .entry(converter.convert(entry, EntryVO.class))
                .traffic(entityUtil.buildTraffic(ENTITY_TYPE, id))
                .cover(CommonImageUtil.getEntryCover(entry.getCover()))
                .build();
    }

    @SneakyThrows
    @Transactional
    public List<EntryMiniVO> getMiniVO(List<Long> ids) {
        List<EntryMiniVO> res = new ArrayList<>();
        List<Entry> entries = listByIds(ids);
        entries.forEach(e -> {
            EntryMiniVO vo = new EntryMiniVO(e);
            vo.setThumb(CommonImageUtil.getEntryThumb(e.getThumb()));
            res.add(vo);
        });
        return res;
    }

    @Transactional
    @SneakyThrows
    public void update(EntryUpdateDTO dto) {
        Entry entry = converter.convert(dto, Entry.class);
        updateById(entry);
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryMiniVO> search(EntrySearchParams param) {
        // 记录开始时间
        long start = System.currentTimeMillis();
        MPJLambdaWrapper<Entry> wrapper = new MPJLambdaWrapper<Entry>().eq(Entry::getStatus, 1)
                .eq(ObjectUtils.isNotEmpty(param.getType()), Entry::getType, param.getType())
                .orderByDesc(Entry::getId);
        if (!param.getKeywords().isEmpty()) {
            if (param.strict()) {
                param.getKeywords().forEach(k ->
                        wrapper.or().eq(Entry::getName, k).or().eq(Entry::getNameZh, k).or().eq(Entry::getNameEn, k));
            } else {
                wrapper.and(w -> param.getKeywords().forEach(k -> w.or(i -> i
                        .apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", k)
                        .or().like(Entry::getName, k)
                        .or().like(Entry::getNameZh, k)
                        .or().like(Entry::getNameEn, k)
                )));
            }
        } else {
            //TODO OPTIMIZE
            if (param.getMode() != 0 && ObjectUtils.isNotEmpty(param.getType())) {
                Set<Long> ids = popularUtil.getEntryPopularityRank(param.getType(), 7);
                if (!ids.isEmpty()) {
                    wrapper.in(Entry::getId, ids).orderBy(true, false, STR."FIELD(id, \{ids.stream()
                            .map(String::valueOf).collect(Collectors.joining(","))})");
                }
            }
        }
        IPage<Entry> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryMiniVO> res = new ArrayList<>(pages.getRecords().stream().map(EntryMiniVO::new).toList());

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize(),
                String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0));
    }


    @SneakyThrows
    @Transactional
    public String uploadImage(long id, int type, MultipartFile file) {
        ImageMiniDTO image = new ImageMiniDTO(type, file);
        Entry entry = getById(id);
        String finalUrl;
        String fieldName;
        if (type == ImageType.MAIN.getValue()) {
            fieldName = "cover";
            if (StringUtils.isNotEmpty(entry.getCover())) {
                qiniuImageUtil.deleteEntryImage(entry.getCover());
            }
        } else if (type == ImageType.THUMB.getValue()) {
            fieldName = "thumb";
            if (StringUtils.isNotEmpty(entry.getThumb())) {
                qiniuImageUtil.deleteEntryImage(entry.getThumb());
            }
        } else {
            throw new Exception();
        }
        //upload new entry image
        finalUrl = qiniuImageUtil.uploadEntryImage(ENTITY_TYPE, id, image);
        //update entry
        update(null, new UpdateWrapper<Entry>().eq("id", id).set(fieldName, finalUrl));
        return finalUrl;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryListVO> list(ListQuery dto) {
        EntryListQueryDTO param = new EntryListQueryDTO(dto);
        MPJLambdaWrapper<Entry> wrapper = new MPJLambdaWrapper<Entry>()
                .selectAll(Entry.class)
                .select(GroupCacheEntryItem::getItems)
                .leftJoin(GroupCacheEntryItem.class,
                        on -> on.eq(GroupCacheEntryItem::getEntryId, Entry::getId)
                )
                .groupBy(Entry::getId)
                .eq(Entry::getType, param.getType())
                .and(StringUtils.isNotEmpty(param.getKeyword()), i -> i
                        .apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword())
                        .or().like(Entry::getName, param.getKeyword())
                        .or().like(Entry::getNameZh, param.getKeyword())
                        .or().like(Entry::getNameEn, param.getKeyword())
                )
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()))
                .orderByDesc(GroupCacheEntryItem::getItems);
        IPage<Entry> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryListVO> res = converter.convert(pages.getRecords(), EntryListVO.class);
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public List<EntryListVO> getSubProducts(long id) {
        List<EntryListVO> res = new ArrayList<>();
        if (MetaData.getOptions().roleSet.isEmpty())
            return res;
        List<Entry> entries = list(
                new MPJLambdaWrapper<Entry>().selectAll(Entry.class)
                        .innerJoin(Relation.class, on -> on.eq(Relation::getEntityId, Entry::getId)
                                .eq(Relation::getRelatedGroup, RelatedGroup.PRODUCT)
                                .eq(Relation::getRelatedEntityType, EntityType.ENTRY)
                                .eq(Relation::getRelatedEntityId, id)
                        ).orderByAsc(Entry::getDate));
        res = converter.convert(entries, EntryListVO.class);
        return res;
    }

}
