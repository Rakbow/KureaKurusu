package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ChangelogMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ChangelogListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.emun.ChangelogField;
import com.rakbow.kureakurusu.data.emun.ChangelogOperate;
import com.rakbow.kureakurusu.data.entity.Changelog;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.data.vo.ChangelogVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryListVO;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/23 14:59
 */
@Service
@RequiredArgsConstructor
public class ChangelogService extends ServiceImpl<ChangelogMapper, Changelog> {

    private final Converter converter;

    @Transactional
    @SneakyThrows
    public SearchResult<ChangelogVO> list(ChangelogListQueryDTO dto) {
        dto.init();
        long start = System.currentTimeMillis();
        IPage<Changelog> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<Changelog>()
                        .eq(Changelog::getEntityType, dto.getEntityType())
                        .eq(Changelog::getEntityId, dto.getEntityId())
                        .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                        .orderByDesc(!dto.isSort(), Changelog::getOperateTime)
        );
        List<ChangelogVO> res = converter.convert(pages.getRecords(), ChangelogVO.class);
        return new SearchResult<>(res, pages.getTotal(), start);
    }

    @Transactional
    @SneakyThrows
    public void create(int type, long id, ChangelogField field, ChangelogOperate operate) {
        save(Changelog
                .builder()
                .entityType(type)
                .entityId(id)
                .field(field)
                .operate(operate)
                .operator(AuthorityInterceptor.getCurrentUser().getUsername())
                .operateTime(DateHelper.now())
                .build()
        );
    }

}
