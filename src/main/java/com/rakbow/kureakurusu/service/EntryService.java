package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.SubjectType;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.vo.EntryMiniVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
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
public class EntryService {

    private final Converter converter;
    private final EntityUtil entityUtil;
    private final PopularUtil popularUtil;

    private final QiniuImageUtil qiniuImageUtil;

    @SneakyThrows
    @Transactional
    public EntryDetailVO detail(int type, long id) {

        BaseMapper<Entry> subMapper = getSubMapper(type);
        Entry entry = subMapper.selectById(id);
        if (entry == null) throw new Exception(I18nHelper.getMessage("entry.url.error"));
        Class<? extends EntryVO> targetVOClass = EntryUtil.getDetailVO(type);
        return EntryDetailVO.builder()
                .entry(converter.convert(entry, targetVOClass))
                .traffic(entityUtil.buildTraffic(type, id))
                .cover(CommonImageUtil.getEntryCover(entry.getCover()))
                .build();
    }

    @Transactional
    @SneakyThrows
    public String update(EntryUpdateDTO dto) {

        Class<? extends Entry> subClass = EntryUtil.getSubClass(dto.getEntityType());
        BaseMapper<Entry> subMapper = MyBatisUtil.getMapper(subClass);

        Entry entry = converter.convert(dto, subClass);
        entry.setEditedTime(DateHelper.now());

        subMapper.updateById(entry);

        return I18nHelper.getMessage("entity.crud.update.success");
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EntryMiniVO> search(int entrySearchType, SimpleSearchParam param) {
        IPage<? extends Entry> pages;
        Integer entityType = EntryUtil.getEntityTypeByEntrySearchType(entrySearchType);
        BaseMapper<Entry> subMapper = getSubMapper(entityType);
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>().eq("status", 1);
        if (entrySearchType == EntrySearchType.CLASSIFICATION.getValue()) {
            wrapper.eq("type", SubjectType.CLASSIFICATION);
        } else if (entrySearchType == EntrySearchType.MATERIAL.getValue()) {
            wrapper.eq("type", SubjectType.MATERIAL);
        } else if (entrySearchType == EntrySearchType.EVENT.getValue()) {
            wrapper.eq("type", SubjectType.EVENT);
        }
        if (!param.keywordEmpty()) {
            if (param.strict()) {
                wrapper.or().eq("name", param.getKeyword())
                        .or().eq("name_zh", param.getKeyword())
                        .or().eq("name_en", param.getKeyword());
            } else {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))" +
                                " LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like("name", param.getKeyword())
                        .or().like("name_zh", param.getKeyword())
                        .or().like("name_en", param.getKeyword());
            }
        } else {
            Set<Long> ids = popularUtil.getPopularityRank(entityType, 5);
            if (!ids.isEmpty()) {
                wrapper.in("id", ids)
                        .orderBy(true, false,
                                STR."FIELD(id, \{ids.stream()
                                        .map(String::valueOf).collect(Collectors.joining(","))})");
            }
        }
        pages = subMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryMiniVO> res = new ArrayList<>(
                pages.getRecords().stream().map(i -> new EntryMiniVO(entityType, i)).toList()
        );

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }


    @SneakyThrows
    @Transactional
    public String uploadImage(int entityType, long entityId, ImageMiniDTO image) {

        //get original entry
        BaseMapper<Entry> subMapper = getSubMapper(entityType);
        Entry entry = subMapper.selectById(entityId);
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
        finalUrl = qiniuImageUtil.uploadEntryImage(entityType, entityId, image);

        UpdateWrapper<Entry> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", entityId).set(fieldName, finalUrl);
        subMapper.update(null, wrapper);
        return finalUrl;
    }

    private BaseMapper<Entry> getSubMapper(int type) {
        Class<? extends Entry> subClass = EntryUtil.getSubClass(type);
        return MyBatisUtil.getMapper(subClass);
    }

}
