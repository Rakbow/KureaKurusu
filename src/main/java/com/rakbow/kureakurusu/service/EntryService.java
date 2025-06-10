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
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.EntryMiniVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryListVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EntryMapper mapper;
    private final RelationMapper relatedMapper;

    private final QiniuImageUtil qiniuImageUtil;
    private final static int ENTITY_TYPE = EntityType.ENTRY.getValue();

    @SneakyThrows
    @Transactional
    public EntryDetailVO detail(long id) {

        Entry entry = mapper.selectById(id);
        if (entry == null) throw new Exception(I18nHelper.getMessage("entry.url.error"));
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
        List<Entry> entries = mapper.selectByIds(ids);
        entries.forEach(e -> {
            EntryMiniVO vo = new EntryMiniVO(e);
            vo.setThumb(CommonImageUtil.getEntryThumb(e.getThumb()));
            res.add(vo);
        });
        return res;
    }

    @Transactional
    @SneakyThrows
    public String update(EntryUpdateDTO dto) {

        Entry entry = converter.convert(dto, Entry.class);
        entry.setEditedTime(DateHelper.now());
        mapper.updateById(entry);
        return I18nHelper.getMessage("entity.crud.update.success");
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryMiniVO> search(EntrySearchParams param) {
        // 记录开始时间
        long start = System.currentTimeMillis();
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>()
                .eq("type", param.getType()).eq("status", 1);
        if (!param.getKeywords().isEmpty()) {
            if (param.strict()) {
                param.getKeywords().forEach(k ->
                        wrapper.or().eq("name", k).or().eq("name_zh", k).or().eq("name_en", k));
            } else {
                param.getKeywords().forEach(k ->
                        wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))" +
                                        " LIKE concat('%', {0}, '%')", k)).or().like("name", k)
                                .or().like("name_zh", k).or().like("name_en", k));
            }
        } else {
            Set<Long> ids = popularUtil.getPopularityRank(param.getType(), 5);
            if (!ids.isEmpty()) {
                wrapper.in("id", ids)
                        .orderBy(true, false,
                                STR."FIELD(id, \{ids.stream()
                                        .map(String::valueOf).collect(Collectors.joining(","))})");
            }
        }
        IPage<Entry> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryMiniVO> res = new ArrayList<>(
                pages.getRecords().stream().map(EntryMiniVO::new).toList()
        );

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize(),
                String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0));
    }


    @SneakyThrows
    @Transactional
    public String uploadImage(long id, ImageMiniDTO image) {

        Entry entry = mapper.selectById(id);
        String finalUrl;
        String fieldName;
        if (image.getType() == ImageType.MAIN.getValue()) {
            fieldName = "cover";
            if (StringUtils.isNotBlank(entry.getCover())) {
                qiniuImageUtil.deleteEntryImage(entry.getCover());
            }
        } else if (image.getType() == ImageType.THUMB.getValue()) {
            fieldName = "thumb";
            if (StringUtils.isNotBlank(entry.getThumb())) {
                qiniuImageUtil.deleteEntryImage(entry.getThumb());
            }
        } else {
            throw new Exception();
        }
        finalUrl = qiniuImageUtil.uploadEntryImage(ENTITY_TYPE, id, image);

        UpdateWrapper<Entry> wrapper = new UpdateWrapper<Entry>()
                .eq("id", id).set(fieldName, finalUrl);
        mapper.update(null, wrapper);
        return finalUrl;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryListVO> list(ListQuery dto) {
        EntryListQueryDTO param = new EntryListQueryDTO(dto);
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>().eq("type", param.getType())
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        if (StringUtils.isNotEmpty(param.getName())) {
            wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))" +
                            " LIKE concat('%', {0}, '%')", param.getName())).or().like("name", param.getName())
                    .or().like("name_zh", param.getName()).or().like("name_en", param.getName());
        }
        IPage<Entry> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryListVO> res = converter.convert(pages.getRecords(), EntryListVO.class);

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public List<EntryListVO> getSubEntries(long id) {
        List<EntryListVO> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<Relation> relations = relatedMapper.selectList(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRelatedGroup, RelatedGroup.RELATED_PRODUCT)
                        .eq(Relation::getRelatedEntityType, EntityType.ENTRY)
                        .eq(Relation::getRelatedEntityId, id)
        );
        if (relations.isEmpty())
            return res;
        List<Long> targetIds = relations.stream().map(Relation::getEntityId).distinct().toList();
        List<Entry> targets = mapper.selectByIds(targetIds);
        targets.sort(DataSorter.entryDateSorter);
        res = converter.convert(targets, EntryListVO.class);
        return res;
    }

}
