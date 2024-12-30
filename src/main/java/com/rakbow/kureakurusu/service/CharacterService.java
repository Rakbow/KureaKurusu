package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.CharaMapper;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Chara;
import com.rakbow.kureakurusu.data.vo.entry.CharacterDetailVO;
import com.rakbow.kureakurusu.data.vo.entry.CharacterVO;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rakbow
 * @since 2024/12/28 4:56
 */
@Service
@RequiredArgsConstructor
public class CharacterService extends ServiceImpl<CharaMapper, Chara> {

    private final Converter converter;
    private final EntityUtil entityUtil;
    private final ResourceService resourceSrv;

    @Transactional
    @SneakyThrows
    public CharacterDetailVO detail(long id) {
        Chara chara = getById(id);
        if (chara == null) throw new Exception(I18nHelper.getMessage("entity.url.error"));
        return CharacterDetailVO.builder()
                .target(converter.convert(chara, CharacterVO.class))
                .traffic(entityUtil.getPageTraffic(EntityType.CHARACTER.getValue(), id))
                .cover(resourceSrv.getEntityCover(EntityType.CHARACTER, chara.getId()))
                .build();
    }

}
