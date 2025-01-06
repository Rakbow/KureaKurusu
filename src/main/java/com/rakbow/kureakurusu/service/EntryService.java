package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(entry == null) throw new Exception(I18nHelper.getMessage("entry.url.error"));
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

}
