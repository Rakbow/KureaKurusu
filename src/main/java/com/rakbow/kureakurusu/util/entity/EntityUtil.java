package com.rakbow.kureakurusu.util.entity;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-04-29 21:15
 * @Description:
 */
public class EntityUtil {

    /**
     * 将数据库实体类companies的json字符串转为JSONArray
     *
     * @param json companies的json字符串
     * @return JSONArray
     * @author rakbow
     */
    // @SuppressWarnings("unchecked")
    // public static JSONArray getCompanies(String json) {
    //
    //     RedisUtil redisUtil = SpringUtil.getBean("redisUtil");
    //
    //     List<JSONObject> companyRoleSet = (List<JSONObject>) redisUtil.get(RedisCacheConstant.COMPANY_ROLE_SET);
    //     List<JSONObject> companySet = (List<JSONObject>) redisUtil.get(RedisCacheConstant.COMPANY_SET);
    //
    //     JSONArray companies = new JSONArray();
    //
    //     CommonUtil.ids2List(publishFormatJson)
    //             .forEach(id -> {
    //                 JSONObject format = DataFinder.findJsonByIdInSet(id, publishFormats);
    //                 if (format != null) {
    //                     publishFormat.add(format);
    //                 }
    //             });
    //
    //     return publishFormat;
    // }

}
