package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.vo.game.GameVO;
import com.rakbow.kureakurusu.data.vo.game.GameVOAlpha;
import com.rakbow.kureakurusu.data.vo.game.GameVOBeta;
import com.rakbow.kureakurusu.data.vo.game.GameVOGamma;
import com.rakbow.kureakurusu.data.entity.Game;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.SpringUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Game VO转换接口
 */
@Mapper(componentModel = "spring")
public interface GameVOMapper {

    /**
     * 获取该类自动生成的实现类的实例
     * 接口中的属性都是 public static final 的 方法都是public abstract的
     */
    GameVOMapper INSTANCES = Mappers.getMapper(GameVOMapper.class);

    /**
     * Game转VO对象，用于详情页面，转换量最大的
     *
     * @param game 游戏
     * @return GameVO
     * @author rakbow
     */
    default GameVO game2VO(Game game) {
        if (game == null) {
            return null;
        }

        GameVO gameVO = new GameVO();

        // //基础信息
        // gameVO.setId(game.getId());
        // gameVO.setName(game.getName());
        // gameVO.setNameZh(game.getNameZh());
        // gameVO.setNameEn(game.getNameEn());
        // gameVO.setBarcode(game.getBarcode());
        // gameVO.setReleaseDate(DateUtil.dateToString(game.getReleaseDate()));
        // gameVO.setHasBonus(game.getHasBonus() == 1);
        // gameVO.setRemark(game.getRemark());
        //
        // //复杂字段
        // gameVO.setRegion(Region.getRegion(game.getRegion()));
        // gameVO.setPlatform(GamePlatform.getAttribute(game.getPlatform()));
        // gameVO.setReleaseType(ReleaseType.getAttribute(game.getReleaseType()));
        // gameVO.setOrganizations(JSON.parseArray(game.getOrganizations()));
        // gameVO.setStaffs(JSON.parseArray(game.getStaffs()));
        // gameVO.setBonus(game.getBonus());

        return gameVO;
    }

    /**
     * Game转VO，供list和index界面使用，信息量较少
     *
     * @param game 游戏
     * @return GameVOAlpha
     * @author rakbow
     */
    default GameVOAlpha game2VOAlpha(Game game) {
        if (game == null) {
            return null;
        }

        GameVOAlpha gameVOAlpha = new GameVOAlpha();

        // //基础信息
        // gameVOAlpha.setId(game.getId());
        // gameVOAlpha.setName(game.getName());
        // gameVOAlpha.setNameZh(game.getNameZh());
        // gameVOAlpha.setNameEn(game.getNameEn());
        // gameVOAlpha.setBarcode(game.getBarcode());
        // gameVOAlpha.setReleaseDate(DateUtil.dateToString(game.getReleaseDate()));
        // gameVOAlpha.setHasBonus(game.getHasBonus() == 1);
        // gameVOAlpha.setRemark(game.getRemark());
        //
        // //关联信息
        // gameVOAlpha.setProducts(EntryUtil.getClassifications(game.getProducts()));
        // gameVOAlpha.setFranchises(EntryUtil.getFranchises(game.getFranchises()));
        //
        // //复杂字段
        // gameVOAlpha.setRegion(Region.getRegion(game.getRegion()));
        // gameVOAlpha.setPlatform(GamePlatform.getAttribute(game.getPlatform()));
        // gameVOAlpha.setReleaseType(ReleaseType.getAttribute(game.getReleaseType()));
        //
        // //将图片分割处理
        // gameVOAlpha.setCover(CommonImageUtil.generateCover(game.getImages(), EntityType.GAME));
        //
        // //审计字段
        // gameVOAlpha.setAddedTime(DateUtil.timestampToString(game.getAddedTime()));
        // gameVOAlpha.setEditedTime(DateUtil.timestampToString(game.getEditedTime()));
        // gameVOAlpha.setStatus(game.getStatus() == 1);

        return gameVOAlpha;
    }

    /**
     * 列表转换, Game转VO对象，供list和index界面使用，信息量较少
     *
     * @param games 游戏列表
     * @return List<GameVOAlpha>
     * @author rakbow
     */
    default List<GameVOAlpha> game2VOAlpha(List<Game> games) {
        List<GameVOAlpha> gameVOAlphas = new ArrayList<>();

        if (!games.isEmpty()) {
            games.forEach(game -> gameVOAlphas.add(game2VOAlpha(game)));
        }

        return gameVOAlphas;
    }

    /**
     * Game转VO对象，信息量最少
     *
     * @param game 游戏
     * @return GameVOBeta
     * @author rakbow
     */
    default GameVOBeta game2VOBeta(Game game) {
        if (game == null) {
            return null;
        }

        GameVOBeta gameVOBeta = new GameVOBeta();

        // //基础信息
        // gameVOBeta.setId(game.getId());
        // gameVOBeta.setName(game.getName());
        // gameVOBeta.setNameZh(game.getNameZh());
        // gameVOBeta.setNameEn(game.getNameEn());
        // gameVOBeta.setReleaseDate(DateUtil.dateToString(game.getReleaseDate()));
        //
        // //复杂字段
        // gameVOBeta.setRegion(Region.getRegion(game.getRegion()));
        // gameVOBeta.setPlatform(GamePlatform.getAttribute(game.getPlatform()));
        // gameVOBeta.setReleaseType(ReleaseType.getAttribute(game.getReleaseType()));
        //
        // //图片
        // gameVOBeta.setCover(CommonImageUtil.generateThumbCover(game.getImages(), EntityType.GAME, 50));
        //
        // //审计字段
        // gameVOBeta.setAddedTime(DateUtil.timestampToString(game.getAddedTime()));
        // gameVOBeta.setEditedTime(DateUtil.timestampToString(game.getEditedTime()));

        return gameVOBeta;
    }

    /**
     * 列表转换, Game转VO对象，信息量最少
     *
     * @param games 游戏列表
     * @return List<GameVOBeta>
     * @author rakbow
     */
    default List<GameVOBeta> game2VOBeta(List<Game> games) {
        List<GameVOBeta> gameVOBetas = new ArrayList<>();

        if (!games.isEmpty()) {
            games.forEach(game -> gameVOBetas.add(game2VOBeta(game)));
        }

        return gameVOBetas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param game game
     * @return GameVOGamma
     * @author rakbow
     */
    default GameVOGamma game2VOGamma(Game game) {
        if (game == null) {
            return null;
        }
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");

        GameVOGamma gameVOGamma = new GameVOGamma();

        // //基础信息
        // gameVOGamma.setId(game.getId());
        // gameVOGamma.setName(game.getName());
        // gameVOGamma.setNameZh(game.getNameZh());
        // gameVOGamma.setNameEn(game.getNameEn());
        // gameVOGamma.setReleaseDate(DateUtil.dateToString(game.getReleaseDate()));
        // gameVOGamma.setHasBonus(game.getHasBonus() == 1);
        //
        // //关联信息
        // gameVOGamma.setProducts(EntryUtil.getClassifications(game.getProducts()));
        // gameVOGamma.setFranchises(EntryUtil.getFranchises(game.getFranchises()));
        //
        // //复杂字段
        // gameVOGamma.setRegion(Region.getRegion(game.getRegion()));
        // gameVOGamma.setPlatform(GamePlatform.getAttribute(game.getPlatform()));
        //
        // gameVOGamma.setCover(QiniuImageUtil.getThumb70Url(game.getImages()));
        //
        // gameVOGamma.setVisitCount(visitUtil.getVisit(EntityType.GAME.getId(), game.getId()));
        // gameVOGamma.setLikeCount(likeUtil.getLike(EntityType.GAME.getId(), game.getId()));

        return gameVOGamma;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param games 列表
     * @return List<GameVOGamma>
     * @author rakbow
     */
    default List<GameVOGamma> game2VOGamma(List<Game> games) {
        List<GameVOGamma> GameVOGammas = new ArrayList<>();

        if (!games.isEmpty()) {
            games.forEach(game -> {
                GameVOGammas.add(game2VOGamma(game));
            });
        }

        return GameVOGammas;
    }

}
