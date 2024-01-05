package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.emun.common.Region;
import com.rakbow.kureakurusu.data.vo.merch.MerchVO;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOAlpha;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOBeta;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOGamma;
import com.rakbow.kureakurusu.data.entity.Merch;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.SpringUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.entry.EntryUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Merch VO转换接口
 */
@Mapper(componentModel = "spring")
public interface MerchVOMapper {

    MerchVOMapper INSTANCES = Mappers.getMapper(MerchVOMapper.class);

    /**
     * Merch转VO对象，用于详情页面，转换量最大的
     *
     * @param merch 周边商品
     * @return MerchVO
     * @author rakbow
     */
    default MerchVO merch2VO(Merch merch) {
        if (merch == null) {
            return null;
        }

        MerchVO merchVO = new MerchVO();

        //基础信息
        merchVO.setId(merch.getId());
        merchVO.setName(merch.getName());
        merchVO.setNameZh(merch.getNameZh());
        merchVO.setNameEn(merch.getNameEn());
        merchVO.setBarcode(merch.getBarcode());
        merchVO.setReleaseDate(DateHelper.dateToString(merch.getReleaseDate()));
        merchVO.setPrice(merch.getPrice());
        merchVO.setCurrencyUnit(merch.getCurrencyUnit());
        merchVO.setNotForSale(merch.getNotForSale() == 1);
        merchVO.setRemark(merch.getRemark());

        //复杂字段
        // merchVO.setCategory(MerchCategory.getAttribute(merch.getCategory()));
        merchVO.setSpec(JSON.parseArray(merch.getSpec()));

        merchVO.setRegion(Region.getRegion(merch.getRegion()));

        return merchVO;
    }

    /**
     * Merch转VO对象，用于list和index页面，转换量较少
     *
     * @param merch 周边商品
     * @return MerchVOAlpha
     * @author rakbow
     */
    default MerchVOAlpha merch2VOAlpha(Merch merch) {
        if (merch == null) {
            return null;
        }

        MerchVOAlpha merchVOAlpha = new MerchVOAlpha();

        //基础信息
        merchVOAlpha.setId(merch.getId());
        merchVOAlpha.setName(merch.getName());
        merchVOAlpha.setNameZh(merch.getNameZh());
        merchVOAlpha.setNameEn(merch.getNameEn());
        merchVOAlpha.setBarcode(merch.getBarcode());
        merchVOAlpha.setReleaseDate(DateHelper.dateToString(merch.getReleaseDate()));
        merchVOAlpha.setPrice(merch.getPrice());
        merchVOAlpha.setCurrencyUnit(merch.getCurrencyUnit());
        merchVOAlpha.setNotForSale(merch.getNotForSale() == 1);
        merchVOAlpha.setRemark(merch.getRemark());

        //关联信息
        merchVOAlpha.setFranchises(EntryUtil.getFranchises(merch.getFranchises()));
        merchVOAlpha.setProducts(EntryUtil.getClassifications(merch.getProducts()));

        //复杂字段
        // merchVOAlpha.setCategory(MerchCategory.getAttribute(merch.getCategory()));

        merchVOAlpha.setRegion(Region.getRegion(merch.getRegion()));

        //图片
//        merchVOAlpha.setCover(CommonImageUtil.generateCover(merch.getImages(), Entity.MERCH));

        //审计字段
        merchVOAlpha.setAddedTime(DateHelper.timestampToString(merch.getAddedTime()));
        merchVOAlpha.setEditedTime(DateHelper.timestampToString(merch.getEditedTime()));
        merchVOAlpha.setStatus(merch.getStatus() == 1);

        return merchVOAlpha;
    }

    /**
     * 列表，Merch转VO对象，用于list和index页面，转换量较少
     *
     * @param merchs 周边商品列表
     * @return List<MerchVOAlpha>
     * @author rakbow
     */
    default List<MerchVOAlpha> merch2VOAlpha(List<Merch> merchs) {
        List<MerchVOAlpha> merchVOAlphas = new ArrayList<>();

        if (!merchs.isEmpty()) {
            merchs.forEach(merch -> merchVOAlphas.add(merch2VOAlpha(merch)));
        }

        return merchVOAlphas;
    }

    /**
     * Merch转VO对象，转换量最少
     *
     * @param merch 周边商品
     * @return MerchVOBeta
     * @author rakbow
     */
    default MerchVOBeta merch2VOBeta(Merch merch) {
        if (merch == null) {
            return null;
        }

        MerchVOBeta merchVOBeta = new MerchVOBeta();

        //基础信息
        merchVOBeta.setId(merch.getId());
        merchVOBeta.setName(merch.getName());
        merchVOBeta.setNameZh(merch.getNameZh());
        merchVOBeta.setNameEn(merch.getNameEn());
        merchVOBeta.setBarcode(merch.getBarcode());
        merchVOBeta.setReleaseDate(DateHelper.dateToString(merch.getReleaseDate()));
        merchVOBeta.setNotForSale(merch.getNotForSale() == 1);

        //关联信息
        merchVOBeta.setFranchises(EntryUtil.getFranchises(merch.getFranchises()));
        merchVOBeta.setProducts(EntryUtil.getClassifications(merch.getProducts()));

        //复杂字段
        // merchVOBeta.setCategory(MerchCategory.getAttribute(merch.getCategory()));

        //图片
//        merchVOBeta.setCover(CommonImageUtil.generateThumbCover(merch.getImages(), Entity.MERCH, 50));

        //审计字段
        merchVOBeta.setAddedTime(DateHelper.timestampToString(merch.getAddedTime()));
        merchVOBeta.setEditedTime(DateHelper.timestampToString(merch.getEditedTime()));

        return merchVOBeta;
    }

    /**
     * 列表，Merch转VO对象，转换量最少
     *
     * @param merchs 周边商品列表
     * @return List<MerchVOBeta>
     * @author rakbow
     */
    default List<MerchVOBeta> merch2VOBeta(List<Merch> merchs) {
        List<MerchVOBeta> merchVOBetas = new ArrayList<>();

        if (!merchs.isEmpty()) {
            merchs.forEach(merch -> merchVOBetas.add(merch2VOBeta(merch)));
        }

        return merchVOBetas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param merch 商品
     * @return MerchVOGamma
     * @author rakbow
     */
    default MerchVOGamma merch2VOGamma(Merch merch) {
        if (merch == null) {
            return null;
        }
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");

        MerchVOGamma merchVOGamma = new MerchVOGamma();

        //基础信息
        merchVOGamma.setId(merch.getId());
        merchVOGamma.setName(merch.getName());
        merchVOGamma.setNameZh(merch.getNameZh());
        merchVOGamma.setNameEn(merch.getNameEn());
        merchVOGamma.setReleaseDate(DateHelper.dateToString(merch.getReleaseDate()));
        merchVOGamma.setNotForSale(merch.getNotForSale() == 1);

        //关联信息
        merchVOGamma.setFranchises(EntryUtil.getFranchises(merch.getFranchises()));
        merchVOGamma.setProducts(EntryUtil.getClassifications(merch.getProducts()));

        //复杂字段
        // merchVOGamma.setCategory(MerchCategory.getAttribute(merch.getCategory()));

        merchVOGamma.setRegion(Region.getRegion(merch.getRegion()));

        merchVOGamma.setCover(QiniuImageUtil.getThumb70Url(merch.getImages()));

//        merchVOGamma.setVisitCount(visitUtil.getVisit(Entity.MERCH.getId(), merch.getId()));
//        merchVOGamma.setLikeCount(likeUtil.getLike(Entity.MERCH.getId(), merch.getId()));

        return merchVOGamma;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param merchs 列表
     * @return List<MerchVOGamma>
     * @author rakbow
     */
    default List<MerchVOGamma> merch2VOGamma(List<Merch> merchs) {
        List<MerchVOGamma> merchVOGammas = new ArrayList<>();

        if (!merchs.isEmpty()) {
            merchs.forEach(merch -> {
                merchVOGammas.add(merch2VOGamma(merch));
            });
        }

        return merchVOGammas;
    }

}
