package com.rakbow.kureakurusu.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.image.ItemDetailInfo;
import com.rakbow.kureakurusu.util.entry.EntryUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Rakbow
 * @since 2023-02-06 16:38
 */
@Component
public class EntityUtil {

    @Resource
    private RedisUtil redisUtil;

    public ItemDetailInfo getItemDetailInfo(Object o, int entityType) {
        ItemDetailInfo detailInfo = new ItemDetailInfo();
        if(o == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(o));
        detailInfo.setId(json.getInteger("id"));

        detailInfo.setDescription(json.getString("description"));
        detailInfo.setProducts(EntryUtil.getClassifications(json.getString("products")));
        detailInfo.setFranchises(EntryUtil.getFranchises(json.getString("franchises")));

        detailInfo.setEntityType(entityType);
        detailInfo.setStatus(json.getIntValue("status") == 1);

        return detailInfo;
    }

    public static ItemDetailInfo getMetaDetailInfo(Object o, int entityType) {
        ItemDetailInfo detailInfo = new ItemDetailInfo();
        if(o == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(o));
        detailInfo.setId(json.getInteger("id"));

        detailInfo.setDescription(json.getString("description"));

        detailInfo.setEntityType(entityType);
        detailInfo.setStatus(json.getIntValue("status") == 1);

        return detailInfo;
    }

    /**
     * 获取页面选项数据
     * @param entityType,entityId,addedTime,editedTime 实体类型，实体id,收录时间,编辑时间
     * @author Rakbow
     */
    public JSONObject getDetailOptions(int entityType) {
        JSONObject options = new JSONObject();
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if(entityType == Entity.ENTRY.getValue()) {
            options.put("entryCategorySet", redisUtil.get(String.format(RedisKey.ENTRY_CATEGORY_SET, lang)));
        }
        if(entityType == Entity.ALBUM.getValue()) {
            options.put("mediaFormatSet", redisUtil.get(String.format(RedisKey.MEDIA_FORMAT_SET, lang)));
            options.put("albumFormatSet", redisUtil.get(String.format(RedisKey.ALBUM_FORMAT_SET, lang)));
            options.put("publishFormatSet", redisUtil.get(String.format(RedisKey.PUBLISH_FORMAT_SET, lang)));
            options.put("companySet", redisUtil.get(String.format(RedisKey.COMPANY_SET, lang)));
            options.put("companyRoleSet", redisUtil.get(String.format(RedisKey.COMPANY_ROLE_SET, lang)));

            options.put("roleSet", redisUtil.get(String.format(RedisKey.ROLE_SET, lang)));
            options.put("personnelSet", redisUtil.get(String.format(RedisKey.PERSONNEL_SET, lang)));
        }
        if(entityType == Entity.BOOK.getValue()) {
            options.put("bookTypeSet", redisUtil.get(String.format(RedisKey.BOOK_TYPE_SET, lang)));
            options.put("regionSet", redisUtil.get(String.format(RedisKey.REGION_SET, lang)));
            options.put("languageSet", redisUtil.get(String.format(RedisKey.LANGUAGE_SET, lang)));
            options.put("companySet", redisUtil.get(String.format(RedisKey.COMPANY_SET, lang)));
            options.put("companyRoleSet", redisUtil.get(String.format(RedisKey.COMPANY_ROLE_SET, lang)));

            options.put("roleSet", redisUtil.get(String.format(RedisKey.ROLE_SET, lang)));
            options.put("personnelSet", redisUtil.get(String.format(RedisKey.PERSONNEL_SET, lang)));
            options.put("specParameterSet", redisUtil.get(String.format(RedisKey.SPEC_PARAM_SET, lang)));
            options.put("publicationSet", redisUtil.get(String.format(RedisKey.PUBLICATION_SET, lang)));
        }
        if(entityType == Entity.DISC.getValue()) {
            options.put("mediaFormatSet", redisUtil.get(String.format(RedisKey.MEDIA_FORMAT_SET, lang)));
            options.put("regionSet", redisUtil.get(String.format(RedisKey.REGION_SET, lang)));
            options.put("specParameterSet", redisUtil.get(String.format(RedisKey.SPEC_PARAM_SET, lang)));
        }
        if(entityType == Entity.GAME.getValue()) {
            options.put("releaseTypeSet", redisUtil.get(String.format(RedisKey.RELEASE_TYPE_SET, lang)));
            options.put("regionSet", redisUtil.get(String.format(RedisKey.REGION_SET, lang)));
            options.put("gamePlatformSet", redisUtil.get(String.format(RedisKey.GAME_PLATFORM_SET, lang)));
        }
//            if(entityType == Entity.MERCH.getId()) {
//                options.put("merchCategorySet", redisUtil.get(RedisCacheConstant.MERCH_CATEGORY_SET_EN));
//                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
//            }
        if(entityType == Entity.MUSIC.getValue()) {
            options.put("audioTypeSet", redisUtil.get(String.format(RedisKey.AUDIO_TYPE_SET, lang)));
        }
//            if(entityType == Entity.PRODUCT.getId()) {
//                options.put("productCategorySet", redisUtil.get(RedisCacheConstant.PRODUCT_CATEGORY_SET_EN));
//                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
//                options.put("platformSet", redisUtil.get(RedisCacheConstant.PLATFORM_SET_EN));
//            }
        options.put("franchiseSet", redisUtil.get(String.format(RedisKey.FRANCHISE_SET, lang)));

        return options;
    }
}
