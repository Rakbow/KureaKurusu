package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rakbow
 * @since 2024/7/2 21:37
 */
@Service
@RequiredArgsConstructor
public class EntryService extends ServiceImpl<EntryMapper, Entry> {

    private final Converter converter;
    private final EntityUtil entityUtil;

    private static final EntityType ENTITY_VALUE = EntityType.ENTRY;

    @SneakyThrows
    @Transactional
    public EntryDetailVO detail(long id) {
        Entry entry = getById(id);
        if(entry == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", EntityType.ENTRY.getLabel()));
        return EntryDetailVO.builder()
                .item(converter.convert(entry, EntryVO.class))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE.getValue(), id))
                .cover(CommonImageUtil.getEntryCover(entry.getImage()))
                .build();
    }

}
