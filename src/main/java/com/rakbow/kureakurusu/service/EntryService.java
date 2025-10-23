package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.result.ItemExtraInfo;
import com.rakbow.kureakurusu.data.vo.entry.*;
import com.rakbow.kureakurusu.data.vo.relation.RelationTargetVO;
import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.ItemUtil;
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

/**
 * @author Rakbow
 * @since 2025/1/7 0:49
 */
@Service
@RequiredArgsConstructor
public class EntryService extends ServiceImpl<EntryMapper, Entry> {

    private final Converter converter;
    private final EntityUtil entityUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final EntryMapper mapper;
    private final RelationService relationSrv;
    private final ChangelogService logSrv;
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
        List<EntrySimpleVO> entries = mapper.selectJoinList(EntrySimpleVO.class,
                new MPJLambdaWrapper<Entry>().in(Entry::getId, ids).selectAsClass(Entry.class, EntrySimpleVO.class)
        );
        return entries.stream().map(EntryMiniVO::new).toList();
    }

    @Transactional
    @SneakyThrows
    public long create(EntrySuperCreateDTO dto) {
        //save entry
        Entry entry = converter.convert(dto.getEntry(), Entry.class);
        save(entry);
        //save related entities
        relationSrv.batchCreate(ENTITY_TYPE, entry.getId(), entry.getType().getValue(), dto.getRelatedEntries());

        logSrv.create(ENTITY_TYPE, entry.getId(), ChangelogField.DEFAULT, ChangelogOperate.CREATE);

        return entry.getId();
    }

    @Transactional
    @SneakyThrows
    public void update(EntryUpdateDTO dto) {
        Entry entry = converter.convert(dto, Entry.class);
        updateById(entry);

        logSrv.create(ENTITY_TYPE, entry.getId(), ChangelogField.BASIC, ChangelogOperate.UPDATE);
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryMiniVO> search(EntrySearchQueryDTO dto) {
        MPJLambdaWrapper<Entry> wrapper = new MPJLambdaWrapper<Entry>()
                .eq(Entry::getStatus, 1)
                .eq(ObjectUtils.isNotEmpty(dto.getType()), Entry::getType, dto.getType())
                .selectAsClass(Entry.class, EntrySimpleVO.class)
                .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                .orderByDesc(!dto.isSort(), Entry::getItems)
                .orderByAsc(!dto.isSort(), Entry::getId);
        if (!dto.getKeywords().isEmpty()) {
            wrapper.and(w -> dto.getKeywords().forEach(k ->
                    w.or(i -> i
                    .apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", k)
                    .or().like(Entry::getName, k)
                    .or().like(Entry::getNameZh, k)
                    .or().like(Entry::getNameEn, k)
            )));
        }
        IPage<EntrySimpleVO> pages = mapper.selectJoinPage(new Page<>(dto.getPage(), dto.getSize()), EntrySimpleVO.class, wrapper);
        List<EntryMiniVO> res = pages.getRecords().stream().map(EntryMiniVO::new).toList();

        return new SearchResult<>(res, pages.getTotal());
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

        logSrv.create(ENTITY_TYPE, entry.getId(), ChangelogField.IMAGE, ChangelogOperate.UPDATE);

        return finalUrl;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryListVO> list(EntryListQueryDTO dto) {
        MPJLambdaWrapper<Entry> wrapper = new MPJLambdaWrapper<Entry>()
                .eq(Entry::getType, dto.getType())
                .and(StringUtils.isNotEmpty(dto.getKeyword()), i -> i
                        .apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", dto.getKeyword())
                        .or().like(Entry::getName, dto.getKeyword())
                        .or().like(Entry::getNameZh, dto.getKeyword())
                        .or().like(Entry::getNameEn, dto.getKeyword())
                )
                .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                .orderByDesc(!dto.isSort(), Entry::getItems);
        IPage<Entry> pages = page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        List<EntryListVO> res = converter.convert(pages.getRecords(), EntryListVO.class);
        return new SearchResult<>(res, pages.getTotal());
    }

    @Transactional
    public List<EntryListVO> getSubProducts(long id) {
        List<EntryListVO> res = new ArrayList<>();
        List<Entry> entries = list(
                new MPJLambdaWrapper<Entry>().selectAll(Entry.class)
                        .innerJoin(Relation.class, on ->
                                on.eq(Relation::getEntityId, Entry::getId)
                                        .eq(Relation::getEntitySubType, EntryType.PRODUCT)
                                        .eq(Relation::getEntityType, EntityType.ENTRY)
                                        .eq(Relation::getRelatedEntityId, id)
                        ).orderByAsc(Entry::getDate));
        if (entries.isEmpty()) return res;
        res = converter.convert(entries, EntryListVO.class);
        return res;
    }

    @Transactional
    public ItemExtraInfo getItemExtraInfo(long id) {
        ItemExtraInfo res = new ItemExtraInfo();
        List<Entry> entries = list(
                new MPJLambdaWrapper<Entry>().selectAll(Entry.class).select(Relation::getRemark)
                        .innerJoin(Relation.class, on -> on.eq(Relation::getRelatedEntityId, Entry::getId)
                                .in(Relation::getRelatedEntityType, EntityType.ENTRY)
                                .in(Relation::getRelatedEntitySubType, ItemUtil.ItemExtraEntitySubTypes)
                                .eq(Relation::getEntityType, EntityType.ITEM)
                                .eq(Relation::getEntityId, id)
                        )
        );
        if (entries.isEmpty())
            return res;
        for (Entry e : entries) {
            RelationVO re = RelationVO.builder()
                    .target(
                            RelationTargetVO.builder()
                                    .entityType(EntityType.ENTRY.getValue())
                                    .entityId(e.getId())
                                    .name(e.getName())
                                    .build()
                    )
                    .remark(e.getRemark())
                    .build();
            if (e.getType() == EntryType.CLASSIFICATION) {
                res.getClassifications().add(re);
            } else if (e.getType() == EntryType.EVENT) {
                res.getEvents().add(re);
            } else if (e.getType() == EntryType.MATERIAL) {
                res.getMaterials().add(re);
            }
        }
        return res;
    }

}
