package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ChangelogMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ChangelogListQueryDTO;
import com.rakbow.kureakurusu.data.enums.ChangelogField;
import com.rakbow.kureakurusu.data.enums.ChangelogOperate;
import com.rakbow.kureakurusu.data.entity.Changelog;
import com.rakbow.kureakurusu.data.vo.ChangelogMiniVO;
import com.rakbow.kureakurusu.data.vo.ChangelogVO;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
        IPage<Changelog> pages = page(
                new Page<>(dto.getPage(), dto.getSize()),
                new MPJLambdaWrapper<Changelog>()
                        .eq(Changelog::getEntityType, dto.getEntityType())
                        .eq(Changelog::getEntityId, dto.getEntityId())
                        .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                        .orderByDesc(!dto.isSort(), Changelog::getOperateTime)
        );
        List<ChangelogVO> res = converter.convert(pages.getRecords(), ChangelogVO.class);
        return new SearchResult<>(res, pages.getTotal());
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

    @Transactional
    @SneakyThrows
    public ChangelogMiniVO mini(int type, long id) {

        ChangelogMiniVO res = new ChangelogMiniVO();
        MPJLambdaWrapper<Changelog> wrapper = new MPJLambdaWrapper<>();
        wrapper.select("count(id) as total", "MIN(operate_time) AS createTime", "MAX(operate_time) AS lastModifyTime")
                .eq(Changelog::getEntityType, type)
                .eq(Changelog::getEntityId, id);

        List<Map<String, Object>> result = listMaps(wrapper);
        res.setCreateTime(DateHelper.timestampToString((Timestamp) result.getFirst().get("createTime")));
        res.setLastModifyTime(DateHelper.timestampToString((Timestamp) result.getFirst().get("lastModifyTime")));
        res.setTotal((long) result.getFirst().get("total"));
        return res;
    }

}
