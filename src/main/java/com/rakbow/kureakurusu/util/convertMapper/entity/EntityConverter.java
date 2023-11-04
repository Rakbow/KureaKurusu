package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.common.MediaFormat;
import com.rakbow.kureakurusu.data.emun.common.Region;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.SpringUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.entry.EntryUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-08 9:42
 * @Description:
 */
public class EntityConverter {

    static String getDate(Date date) {
        return DateHelper.dateToString(date);
    }

    static List<Attribute<Integer>> getProducts(String products) {
        return EntryUtil.getClassifications(products);
    }

    static List<Attribute<Integer>> getFranchises(String franchises) {
        return EntryUtil.getFranchises(franchises);
    }

    static List<Attribute<Integer>> getMediaFormat(String formats) {
        return MediaFormat.getAttributes(formats);
    }

    static String getVOTime(Timestamp timestamp) {
        return DateHelper.timestampToString(timestamp);
    }

    static boolean getBool(int bool) {
        return bool == 1;
    }

    static String getThumb70Cover(String images) {
        return QiniuImageUtil.getThumb70Url(images);
    }

    static JSONArray getCompanies(String json) {
        return EntryUtil.getCompanies(json);
    }

    static JSONArray getPersonnel(String json) {
        return EntryUtil.getPersonnel(json);
    }

    static JSONArray getSpecs(String json) {
        return EntryUtil.getSpecs(json);
    }

    static JSONObject getCover(String images, Entity entity) {
        return CommonImageUtil.generateCover(images, entity);
    }

    static JSONObject getThumbCover(String images, Entity entity, int size) {
        return CommonImageUtil.generateThumbCover(images, entity, size);
    }

    static long getVisitCount(int entityTypeId, int id) {
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        return visitUtil.getVisit(entityTypeId, id);
    }

    static long getLikeCount(int entityTypeId, int id) {
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");
        return likeUtil.getLike(entityTypeId, id);
    }

    static String getCurrencyUnitByCode(String region) {
        return Region.getCurrencyUnitByCode(region);
    }

    static RegionVO getRegion(String region) {
        return Region.getRegion(region);
    }

    static JSONArray getJSONArray(String json) {
        return JSON.parseArray(json);
    }

}
