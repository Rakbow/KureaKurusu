package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.vo.disc.DiscVO;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOAlpha;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOBeta;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOGamma;
import com.rakbow.kureakurusu.entity.Disc;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.SpringUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-12 10:45
 * @Description: Disc VO转换接口
 */
@Mapper(componentModel = "spring")
public interface DiscVOMapper {

    DiscVOMapper INSTANCES = Mappers.getMapper(DiscVOMapper.class);

    /**
     * Disc转VO对象，用于详情页面，转换量最大的
     *
     * @param disc Disc
     * @return DiscVO
     * @author rakbow
     */
    default DiscVO disc2VO(Disc disc) {
        if (disc == null) {
            return null;
        }

        DiscVO discVO = new DiscVO();

        // discVO.setId(disc.getId());
        // discVO.setCatalogNo(disc.getCatalogNo());
        // discVO.setName(disc.getName());
        // discVO.setNameZh(disc.getNameZh());
        // discVO.setNameEn(disc.getNameEn());
        // discVO.setBarcode(disc.getBarcode());
        // discVO.setReleaseDate(DateUtil.dateToString(disc.getReleaseDate()));
        // discVO.setPrice(disc.getPrice());
        // discVO.setCurrencyUnit(disc.getCurrencyUnit());
        // discVO.setLimited(disc.getLimited() == 1);
        // discVO.setHasBonus(disc.getHasBonus() == 1);
        // discVO.setRemark(disc.getRemark());
        //
        // discVO.setRegion(Region.getRegion(disc.getRegion()));
        //
        // discVO.setMediaFormat(MediaFormat.getAttributes(disc.getMediaFormat()));
        // discVO.setSpec(JSON.parseArray(disc.getSpec()));
        // discVO.setBonus(disc.getBonus());

        return discVO;
    }

    /**
     * Disc转VO对象，用于list和index页面，转换量较少
     *
     * @param disc Disc
     * @return DiscVOAlpha
     * @author rakbow
     */
    default DiscVOAlpha disc2VOAlpha(Disc disc) {
        if (disc == null) {
            return null;
        }

        DiscVOAlpha discVOAlpha = new DiscVOAlpha();

        // discVOAlpha.setId(disc.getId());
        // discVOAlpha.setCatalogNo(disc.getCatalogNo());
        // discVOAlpha.setName(disc.getName());
        // discVOAlpha.setNameZh(disc.getNameZh());
        // discVOAlpha.setNameEn(disc.getNameEn());
        // discVOAlpha.setBarcode(disc.getBarcode());
        // discVOAlpha.setReleaseDate(DateUtil.dateToString(disc.getReleaseDate()));
        // discVOAlpha.setPrice(disc.getPrice());
        // discVOAlpha.setCurrencyUnit(disc.getCurrencyUnit());
        // discVOAlpha.setLimited(disc.getLimited() == 1);
        // discVOAlpha.setHasBonus(disc.getHasBonus() == 1);
        // discVOAlpha.setRemark(disc.getRemark());
        //
        // discVOAlpha.setRegion(Region.getRegion(disc.getRegion()));
        //
        // discVOAlpha.setFranchises(EntryUtil.getFranchises(disc.getFranchises()));
        // discVOAlpha.setProducts(EntryUtil.getClassifications(disc.getProducts()));
        //
        // discVOAlpha.setMediaFormat(MediaFormat.getAttributes(disc.getMediaFormat()));
        //
        // //将图片分割处理
        // discVOAlpha.setCover(CommonImageUtil.generateCover(disc.getImages(), EntityType.DISC));
        //
        // discVOAlpha.setAddedTime(DateUtil.timestampToString(disc.getAddedTime()));
        // discVOAlpha.setEditedTime(DateUtil.timestampToString(disc.getEditedTime()));
        // discVOAlpha.setStatus(disc.getStatus() == 1);

        return discVOAlpha;
    }


    /**
     * 列表，Disc转VO对象，用于list和index页面，转换量较少
     *
     * @param discs disc列表
     * @return List<DiscVOAlpha>
     * @author rakbow
     */
    default List<DiscVOAlpha> disc2VOAlpha(List<Disc> discs) {
        List<DiscVOAlpha> discVOAlphas = new ArrayList<>();

        if (!discs.isEmpty()) {
            discs.forEach(disc -> discVOAlphas.add(disc2VOAlpha(disc)));
        }

        return discVOAlphas;
    }

    /**
     * Disc转VO对象，转换量最少
     *
     * @param disc disc
     * @return DiscVOBeta
     * @author rakbow
     */
    default DiscVOBeta disc2VOBeta(Disc disc) {
        if (disc == null) {
            return null;
        }

        DiscVOBeta discVOBeta = new DiscVOBeta();

        // discVOBeta.setId(disc.getId());
        // discVOBeta.setCatalogNo(disc.getCatalogNo());
        // discVOBeta.setName(disc.getName());
        // discVOBeta.setNameZh(disc.getNameZh());
        // discVOBeta.setNameEn(disc.getNameEn());
        // discVOBeta.setReleaseDate(DateUtil.dateToString(disc.getReleaseDate()));
        //
        // discVOBeta.setMediaFormat(MediaFormat.getAttributes(disc.getMediaFormat()));
        //
        // discVOBeta.setCover(CommonImageUtil.generateThumbCover(disc.getImages(), EntityType.DISC, 50));
        //
        // discVOBeta.setAddedTime(DateUtil.timestampToString(disc.getAddedTime()));
        // discVOBeta.setEditedTime(DateUtil.timestampToString(disc.getEditedTime()));

        return discVOBeta;
    }

    /**
     * 列表，Disc转VO对象，转换量最少
     *
     * @param discs disc列表
     * @return List<DiscVOBeta>
     * @author rakbow
     */
    default List<DiscVOBeta> disc2VOBeta(List<Disc> discs) {
        List<DiscVOBeta> discVOBetas = new ArrayList<>();

        if (!discs.isEmpty()) {
            discs.forEach(disc -> discVOBetas.add(disc2VOBeta(disc)));
        }

        return discVOBetas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param disc disc
     * @return DiscVOGamma
     * @author rakbow
     */
    default DiscVOGamma disc2VOGamma(Disc disc) {
        if (disc == null) {
            return null;
        }
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");

        DiscVOGamma discVOGamma = new DiscVOGamma();

        // discVOGamma.setId(disc.getId());
        // discVOGamma.setCatalogNo(disc.getCatalogNo());
        // discVOGamma.setName(disc.getName());
        // discVOGamma.setNameZh(disc.getNameZh());
        // discVOGamma.setNameEn(disc.getNameEn());
        // discVOGamma.setReleaseDate(DateUtil.dateToString(disc.getReleaseDate()));
        // discVOGamma.setLimited(disc.getLimited() == 1);
        // discVOGamma.setHasBonus(disc.getHasBonus() == 1);
        //
        // discVOGamma.setRegion(Region.getRegion(disc.getRegion()));
        //
        // discVOGamma.setFranchises(EntryUtil.getFranchises(disc.getFranchises()));
        // discVOGamma.setProducts(EntryUtil.getClassifications(disc.getProducts()));
        //
        // discVOGamma.setMediaFormat(MediaFormat.getAttributes(disc.getMediaFormat()));
        //
        // discVOGamma.setCover(QiniuImageUtil.getThumb70Url(disc.getImages()));
        //
        // discVOGamma.setVisitCount(visitUtil.getVisit(EntityType.DISC.getId(), disc.getId()));
        // discVOGamma.setLikeCount(likeUtil.getLike(EntityType.DISC.getId(), disc.getId()));

        return discVOGamma;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param discs 列表
     * @return List<DiscVOGamma>
     * @author rakbow
     */
    default List<DiscVOGamma> disc2VOGamma(List<Disc> discs) {
        List<DiscVOGamma> discVOGammas = new ArrayList<>();

        if (!discs.isEmpty()) {
            discs.forEach(disc -> {
                discVOGammas.add(disc2VOGamma(disc));
            });
        }

        return discVOGammas;
    }

}
