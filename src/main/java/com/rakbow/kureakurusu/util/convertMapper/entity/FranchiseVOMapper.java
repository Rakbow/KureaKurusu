package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.entity.franchise.MetaInfo;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseVO;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseVOAlpha;
import com.rakbow.kureakurusu.data.vo.franchise.ParentFranchiseVO;
import com.rakbow.kureakurusu.entity.Franchise;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.entity.FranchiseUtil;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Franchise VO转换接口
 */
@Mapper(componentModel = "spring")
public interface FranchiseVOMapper {

    FranchiseVOMapper INSTANCES = Mappers.getMapper(FranchiseVOMapper.class);

    /**
     * Franchise转VO对象，用于详情页面，转换量最大的
     *
     * @param franchise 元系列
     * @return FranchiseVO
     * @author rakbow
     */
    default FranchiseVO franchise2VO(Franchise franchise) {

        FranchiseVO franchiseVO = new FranchiseVO();

        franchiseVO.setId(franchise.getId());
        franchiseVO.setName(franchise.getName());
        franchiseVO.setNameZh(franchise.getNameZh());
        franchiseVO.setNameEn(franchise.getNameEn());
        franchiseVO.setOriginDate(DateHelper.dateToString(franchise.getOriginDate()));
        franchiseVO.setRemark(franchise.getRemark());

        MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
        if(StringUtils.equals(metaInfo.isMeta, "1")) {
            franchiseVO.setMetaLabel(true);
            franchiseVO.setChildFranchises(FranchiseUtil.getChildFranchiseIds(franchise));
            franchiseVO.setChildFranchiseInfos(FranchiseUtil.getChildFranchises(franchise));
        }else {
            franchiseVO.setMetaLabel(false);
        }
        if(!StringUtils.equals(metaInfo.metaId, "0")) {
            franchiseVO.setParentFranchise(franchise2ParentVO(FranchiseUtil.getParentFranchise(franchise)));
        }

        return franchiseVO;
    }

    /**
     * Franchise转VO，供list界面使用，信息量较少
     *
     * @param franchise 元系列
     * @return FranchiseVOAlpha
     * @author rakbow
     */
    default FranchiseVOAlpha franchise2VOAlpha(Franchise franchise) {

        FranchiseVOAlpha franchiseVOAlpha = new FranchiseVOAlpha();

        franchiseVOAlpha.setId(franchise.getId());
        franchiseVOAlpha.setName(franchise.getName());
        franchiseVOAlpha.setNameZh(franchise.getNameZh());
        franchiseVOAlpha.setNameEn(franchise.getNameEn());
        franchiseVOAlpha.setOriginDate(DateHelper.dateToString(franchise.getOriginDate()));
        franchiseVOAlpha.setRemark(franchise.getRemark());

//        franchiseVOAlpha.setCover(CommonImageUtil.generateCover(franchise.getImages(), Entity.FRANCHISE));

        franchiseVOAlpha.setAddedTime(DateHelper.timestampToString(franchise.getAddedTime()));
        franchiseVOAlpha.setEditedTime(DateHelper.timestampToString(franchise.getEditedTime()));

        MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());

        if(StringUtils.equals(metaInfo.isMeta, "1")) {
            franchiseVOAlpha.setMetaLabel(true);
            franchiseVOAlpha.setChildFranchises(FranchiseUtil.getChildFranchiseIds(franchise));
        }else {
            franchiseVOAlpha.setMetaLabel(false);
        }
        if(!StringUtils.equals(metaInfo.metaId, "0")) {
            franchiseVOAlpha.setMetaId(Integer.parseInt(metaInfo.metaId));
        }

        franchiseVOAlpha.setStatus(franchise.getStatus() == 1);

        return franchiseVOAlpha;
    }

    /**
     * 列表转换, Franchise转VO对象，供list界面使用，信息量较少
     *
     * @param franchises 元系列列表
     * @return List<FranchiseVOAlpha>
     * @author rakbow
     */
    default List<FranchiseVOAlpha> franchise2VOAlpha(List<Franchise> franchises) {
        List<FranchiseVOAlpha> franchiseVOAlphas = new ArrayList<>();

        if (!franchises.isEmpty()) {
            franchises.forEach(franchise -> franchiseVOAlphas.add(franchise2VOAlpha(franchise)));
        }

        return franchiseVOAlphas;
    }


    /**
     * Franchise转父级系列VO
     *
     * @param franchise 元系列
     * @return FranchiseVOAlpha
     * @author rakbow
     */
    default ParentFranchiseVO franchise2ParentVO(Franchise franchise) {

        ParentFranchiseVO parentFranchiseVO = new ParentFranchiseVO();

        parentFranchiseVO.setId(franchise.getId());
        parentFranchiseVO.setName(franchise.getName());
        parentFranchiseVO.setNameZh(franchise.getNameZh());
        parentFranchiseVO.setNameEn(franchise.getNameEn());

        return parentFranchiseVO;
    }

    default List<ParentFranchiseVO> franchise2ParentVO(List<Franchise> franchises) {
        List<ParentFranchiseVO> parentFranchiseVOs = new ArrayList<>();

        if (!franchises.isEmpty()) {
            franchises.forEach(franchise -> parentFranchiseVOs.add(franchise2ParentVO(franchise)));
        }

        return parentFranchiseVOs;
    }

}
