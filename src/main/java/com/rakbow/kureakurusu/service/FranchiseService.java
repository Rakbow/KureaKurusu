package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.FranchiseMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.FranchiseDetailQry;
import com.rakbow.kureakurusu.data.dto.FranchiseListParams;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseDetailVO;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseVO;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.FranchiseVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rakbow
 * @since 2022-08-20 1:17
 */
@RequiredArgsConstructor
@Service
public class FranchiseService extends ServiceImpl<FranchiseMapper, Franchise> {

    private final FranchiseMapper mapper;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final FranchiseVOMapper voMapper;
    private final int ENTITY_VALUE = Entity.FRANCHISE.getValue();

    @SneakyThrows
    @Transactional
    public FranchiseDetailVO detail(FranchiseDetailQry qry) {
        Franchise item = getById(qry.getId());
        if (item == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.FRANCHISE.getName()));

        return FranchiseDetailVO.builder()
                .item(voMapper.toVO(item))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .itemImageInfo(CommonImageUtil.segmentImages(item.getImages(), 200, Entity.FRANCHISE, false))
                .build();
    }

    @Transactional
    public SearchResult<FranchiseVO> list(FranchiseListParams param) {
        QueryWrapper<Franchise> wrapper = new QueryWrapper<Franchise>()
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .orderBy(param.isSort(), param.asc(), param.sortField);
        IPage<Franchise> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<FranchiseVO> items = voMapper.toVO(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public void delete(List<Long> ids) {
        //get original data
        List<Franchise> items = mapper.selectBatchIds(ids);
        for (Franchise item : items) {
            //delete image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete entity
            mapper.deleteById(item.getId());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, item.getId());
        }
    }
}
