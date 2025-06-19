package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.RelatedGroup;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.EntryMiniVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryListVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final RelationMapper relatedMapper;

    private final QiniuImageUtil qiniuImageUtil;
    private final static int ENTITY_TYPE = EntityType.ENTRY.getValue();

    @SneakyThrows
    @Transactional
    public EntryDetailVO detail(long id) {

        Entry entry = getById(id);
        if (entry == null) throw ErrorFactory.entryNull();
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
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>()
                .eq("type", param.getType()).eq("status", 1)
                .orderByAsc("id");
        if (!param.getKeywords().isEmpty()) {
            if (param.strict()) {
                param.getKeywords().forEach(k ->
                        wrapper.or().eq("name", k).or().eq("name_zh", k).or().eq("name_en", k));
            } else {
                wrapper.and(w -> param.getKeywords().forEach(k -> w.or(i -> i
                        .apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", k)
                        .or().like("name", k)
                        .or().like("name_zh", k)
                        .or().like("name_en", k)
                )));
            }
        }
        // else {
        //     Set<Long> ids = popularUtil.getPopularityRank(param.getType(), 5);
        //     if (!ids.isEmpty()) {
        //         wrapper.in("id", ids)
        //                 .orderBy(true, false,
        //                         STR."FIELD(id, \{ids.stream()
        //                                 .map(String::valueOf).collect(Collectors.joining(","))})");
        //     }
        // }
        IPage<Entry> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryMiniVO> res = new ArrayList<>(
                pages.getRecords().stream().map(EntryMiniVO::new).toList()
        );

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
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>().eq("type", param.getType())
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        if (StringUtils.isNotEmpty(param.getName())) {
            wrapper.and(i -> i
                    .apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getName())
                    .or().like("name", param.getName())
                    .or().like("name_zh", param.getName())
                    .or().like("name_en", param.getName())
            );
        }
        IPage<Entry> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryListVO> res = converter.convert(pages.getRecords(), EntryListVO.class);
        getEntryRelatedItemCount(res);
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public List<EntryListVO> getSubEntries(long id) {
        List<EntryListVO> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<Relation> relations = relatedMapper.selectList(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRelatedGroup, RelatedGroup.PRODUCT)
                        .eq(Relation::getRelatedEntityType, EntityType.ENTRY)
                        .eq(Relation::getRelatedEntityId, id)
        );
        if (relations.isEmpty())
            return res;
        List<Long> targetIds = relations.stream().map(Relation::getEntityId).distinct().toList();
        List<Entry> targets = listByIds(targetIds);
        targets.sort(DataSorter.entryDateSorter);
        res = converter.convert(targets, EntryListVO.class);
        return res;
    }

    @Transactional
    @SneakyThrows
    public void getEntryRelatedItemCount(List<EntryListVO> entries) {
        if(entries.isEmpty()) return;
        List<Long> ids = entries.stream().map(EntryListVO::getId).toList();
        QueryWrapper<Relation> wrapper = new QueryWrapper<Relation>()
                .select("related_entity_id AS entity_id", "COUNT(id) AS count")
                .eq("related_entity_type", EntityType.ENTRY)
                .eq("entity_type", EntityType.ITEM)
                .in("related_entity_id", ids)
                .groupBy("related_entity_type", "related_entity_id");
        List<Map<String, Object>> maps = relatedMapper.selectMaps(wrapper);
        List<EntityRelatedCount> relatedCountList = JsonUtil.to(maps, EntityRelatedCount.class);
        Map<Long, Integer> countMap = relatedCountList.stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        for(EntryListVO e : entries) {
            e.setItemCount(countMap.getOrDefault(e.getId(), 0));
        }
    }

}
