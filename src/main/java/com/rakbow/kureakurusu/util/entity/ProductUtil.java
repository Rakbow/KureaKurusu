package com.rakbow.kureakurusu.util.entity;

import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.entity.product.ProductCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-11-26 14:28
 */

public class ProductUtil {

    /**
     * 通过实体类型id获取作品选项包含的作品类型
     *
     * @param entityType
     * @return JSONObject
     * @author rakbow
     */
    public static List<Integer> getCategoriesByEntityType (int entityType) {

        List<Integer> categories = new ArrayList<>();
//        if (entityType == Entity.PRODUCT.getId()) {
//            categories.add(ProductCategory.UNCATEGORIZED.getId());
//            categories.add(ProductCategory.GAME.getId());
//            categories.add(ProductCategory.ANIMATION.getId());
//            categories.add(ProductCategory.OVA_OAD.getId());
//            categories.add(ProductCategory.TV_SERIES.getId());
//            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
//            categories.add(ProductCategory.NOVEL.getId());
//            categories.add(ProductCategory.MANGA.getId());
//            categories.add(ProductCategory.PUBLICATION.getId());
//            categories.add(ProductCategory.MISC.getId());
//        }
        if (entityType == Entity.ALBUM.getValue()) {
            categories.add(ProductCategory.GAME.getValue());
            categories.add(ProductCategory.ANIME_TV.getValue());
            categories.add(ProductCategory.OVA_OAD.getValue());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getValue());
            categories.add(ProductCategory.TV_SERIES.getValue());
        }
        if (entityType == Entity.BOOK.getValue()) {
            categories.add(ProductCategory.NOVEL.getValue());
            categories.add(ProductCategory.MANGA.getValue());
            categories.add(ProductCategory.PUBLICATION.getValue());
        }
        if (entityType == Entity.DISC.getValue()) {
            categories.add(ProductCategory.ANIME_TV.getValue());
            categories.add(ProductCategory.OVA_OAD.getValue());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getValue());
            categories.add(ProductCategory.TV_SERIES.getValue());
        }
        if (entityType == Entity.GAME.getValue()) {
            categories.add(ProductCategory.GAME.getValue());
        }
//        if (entityType == Entity.MERCH.getId()) {
//            categories.add(ProductCategory.GAME.getId());
//            categories.add(ProductCategory.ANIMATION.getId());
//            categories.add(ProductCategory.OVA_OAD.getId());
//            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
//            categories.add(ProductCategory.TV_SERIES.getId());
//            categories.add(ProductCategory.NOVEL.getId());
//            categories.add(ProductCategory.MANGA.getId());
//            categories.add(ProductCategory.PUBLICATION.getId());
//        }
        return categories;
    }

}
