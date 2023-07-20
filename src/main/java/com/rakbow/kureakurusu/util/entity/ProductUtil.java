package com.rakbow.kureakurusu.util.entity;

import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.entity.product.ProductCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-26 14:28
 * @Description:
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
        if (entityType == Entity.ALBUM.getId()) {
            categories.add(ProductCategory.GAME.getId());
            categories.add(ProductCategory.ANIMATION.getId());
            categories.add(ProductCategory.OVA_OAD.getId());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
            categories.add(ProductCategory.TV_SERIES.getId());
        }
        if (entityType == Entity.BOOK.getId()) {
            categories.add(ProductCategory.NOVEL.getId());
            categories.add(ProductCategory.MANGA.getId());
            categories.add(ProductCategory.PUBLICATION.getId());
        }
        if (entityType == Entity.DISC.getId()) {
            categories.add(ProductCategory.ANIMATION.getId());
            categories.add(ProductCategory.OVA_OAD.getId());
            categories.add(ProductCategory.LIVE_ACTION_MOVIE.getId());
            categories.add(ProductCategory.TV_SERIES.getId());
        }
        if (entityType == Entity.GAME.getId()) {
            categories.add(ProductCategory.GAME.getId());
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
