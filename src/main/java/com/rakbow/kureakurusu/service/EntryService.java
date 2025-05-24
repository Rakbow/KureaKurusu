package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.dto.EntryListQueryDTO;
import com.rakbow.kureakurusu.data.dto.EntrySearchParams;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.SubjectType;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.dto.EntityMinDTO;
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

    @SneakyThrows
    @Transactional
    public List<EntryMiniVO> getMiniVO(List<EntityMinDTO> entries) {
        List<EntryMiniVO> res = new ArrayList<>();
        BaseMapper<Entry> subMapper;
        for (EntityMinDTO e : entries) {
            subMapper = getSubMapper(e.getEntityType());
            Entry entry = subMapper.selectById(e.getEntityId());
            if (entry == null) continue;
            EntryMiniVO vo = new EntryMiniVO(e.getEntityType(), entry);
            vo.setThumb(CommonImageUtil.getEntryThumb(entry.getThumb()));
            res.add(vo);
        }
        return res;
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
    public SearchResult<EntryMiniVO> search(EntrySearchParams param) {
        // 记录开始时间
        long start = System.currentTimeMillis();
        IPage<? extends Entry> pages;
        Integer entityType = EntryUtil.getEntityTypeByEntrySearchType(param.getSearchType());
        BaseMapper<Entry> subMapper = getSubMapper(entityType);
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>().eq("status", 1);
        if (param.getSearchType() == EntrySearchType.CLASSIFICATION.getValue()) {
            wrapper.eq("type", SubjectType.CLASSIFICATION);
        } else if (param.getSearchType() == EntrySearchType.MATERIAL.getValue()) {
            wrapper.eq("type", SubjectType.MATERIAL);
        } else if (param.getSearchType() == EntrySearchType.EVENT.getValue()) {
            wrapper.eq("type", SubjectType.EVENT);
        }
        if (!param.getKeywords().isEmpty()) {
            if (param.strict()) {
                param.getKeywords().forEach(k -> {
                    wrapper.or().eq("name", k).or().eq("name_zh", k).or().eq("name_en", k);
                });
            } else {
                param.getKeywords().forEach(k -> {
                    wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))" +
                                    " LIKE concat('%', {0}, '%')", k)).or().like("name", k)
                            .or().like("name_zh", k).or().like("name_en", k);
                });
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

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize(),
                String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0));
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

    @Transactional
    @SneakyThrows
    public SearchResult<? extends EntryListVO> list(ListQuery dto) {
        EntryListQueryDTO param = EntryUtil.getEntryListQueryDTO(dto);
        Class<? extends EntryListVO> entryListVOClass = EntryUtil.getEntryListVO(param.getSearchType());
        Integer entityType = EntryUtil.getEntityTypeByEntrySearchType(param.getSearchType());
        BaseMapper<Entry> subMapper = getSubMapper(entityType);
        QueryWrapper<Entry> wrapper = new QueryWrapper<Entry>()
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        if (param.getSearchType() == EntrySearchType.CLASSIFICATION.getValue()) {
            wrapper.eq("type", SubjectType.CLASSIFICATION);
        } else if (param.getSearchType() == EntrySearchType.MATERIAL.getValue()) {
            wrapper.eq("type", SubjectType.MATERIAL);
        } else if (param.getSearchType() == EntrySearchType.EVENT.getValue()) {
            wrapper.eq("type", SubjectType.EVENT);
        }
        if (param.getName() != null && !param.getName().isEmpty()) {
            wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))" +
                            " LIKE concat('%', {0}, '%')", param.getName())).or().like("name", param.getName())
                    .or().like("name_zh", param.getName()).or().like("name_en", param.getName());
        }
        IPage<? extends Entry> pages = subMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<? extends EntryListVO> res = converter.convert(pages.getRecords(), entryListVOClass);

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

}
