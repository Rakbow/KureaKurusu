package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.emun.SubjectType;
import com.rakbow.kureakurusu.data.entity.entry.*;
import com.rakbow.kureakurusu.data.vo.EntryMiniVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 0:49
 */
@Service
@RequiredArgsConstructor
public class EntryService {

    private final Converter converter;
    private final EntityUtil entityUtil;

    @SneakyThrows
    @Transactional
    public EntryDetailVO detail(int type, long id) {

        Class<? extends Entry> subClass = EntryUtil.getSubClass(type);
        BaseMapper<Entry> subMapper = MyBatisUtil.getMapper(subClass);
        Entry entry = subMapper.selectById(id);
        if (entry == null) throw new Exception(I18nHelper.getMessage("entry.url.error"));
        Class<? extends EntryVO> targetVOClass = EntryUtil.getDetailVO(type);
        return EntryDetailVO.builder()
                .entry(converter.convert(entry, targetVOClass))
                .traffic(entityUtil.getPageTraffic(type, id))
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
        Class<? extends Entry> subClass = EntryUtil.getSubClass(entityType);
        BaseMapper<Entry> subMapper = MyBatisUtil.getMapper(subClass);
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>()
                .eq("status", 1)
                .orderByAsc("id");
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
        }
        pages = subMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EntryMiniVO> res = new ArrayList<>(
                pages.getRecords().stream().map(i -> new EntryMiniVO(entityType, i)).toList()
        );

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

}
