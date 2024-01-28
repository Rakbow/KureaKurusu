package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.FranchiseMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseDetailQry;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseDetailVO;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseVO;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.FranchiseVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rakbow
 * @since 2022-08-20 1:17
 */
@RequiredArgsConstructor
@Service
public class FranchiseService extends ServiceImpl<FranchiseMapper, Franchise> {

    private final FranchiseMapper mapper;
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final FranchiseVOMapper voMapper;
    private final int ENTITY_VALUE = Entity.FRANCHISE.getValue();

    @SneakyThrows
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

    public SearchResult<FranchiseVO> list(QueryParams param) {
        String name = param.getStr("name");
        String nameZh = param.getStr("nameZh");
        String nameEn = param.getStr("nameEn");
        LambdaQueryWrapper<Franchise> wrapper = new LambdaQueryWrapper<Franchise>()
                .like(!StringUtils.isBlank(name), Franchise::getName, name)
                .like(!StringUtils.isBlank(nameZh), Franchise::getNameZh, nameZh)
                .like(!StringUtils.isBlank(nameEn), Franchise::getNameEn, nameEn);

        if(!StringUtils.isBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.asc(), Franchise::getName);
                case "nameZh" -> wrapper.orderBy(true, param.asc(), Franchise::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.asc(), Franchise::getNameEn);
            }
        }else {
            wrapper.orderByDesc(Franchise::getId);
        }

        IPage<Franchise> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<FranchiseVO> items = voMapper.toVO(pages.getRecords());

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    public void delete(List<Long> ids) {
        //get original data
        List<Franchise> items = mapper.selectBatchIds(ids);
        for (Franchise item : items) {
            //delete image
            qiniuFileUtil.deleteAllImage(item.getImages());
            //delete entity
            mapper.deleteById(item.getId());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, item.getId());
        }
    }

    public void update(FranchiseUpdateDTO dto) {
        update(
                new LambdaUpdateWrapper<Franchise>()
                        .eq(Franchise::getId, dto.getId())
                        .set(Franchise::getName, dto.getName())
                        .set(Franchise::getNameZh, dto.getNameZh())
                        .set(Franchise::getNameEn, dto.getNameEn())
                        .set(Franchise::getEditedTime, DateHelper.now())
        );
    }
}
