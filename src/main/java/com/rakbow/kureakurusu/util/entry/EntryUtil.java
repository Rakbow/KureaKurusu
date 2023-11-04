package com.rakbow.kureakurusu.util.entry;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.common.CompanyRole;
import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.SpringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-02 1:04
 * @Description:
 */
@Component
public class EntryUtil {

    public static JSONArray getCompanies(String json) {
        JSONArray orgCompanies = JSON.parseArray(json);
        if(orgCompanies.isEmpty()) return new JSONArray();

        List<Attribute<Integer>> allCompanies = getAttributesForRedis(EntryCategory.COMPANY);

        JSONArray companies = new JSONArray();
        for (int i = 0; i < orgCompanies.size(); i++) {
            JSONObject orgCompany = orgCompanies.getJSONObject(i);
            JSONObject company = new JSONObject();

            company.put("role", EnumUtil.getAttribute(CompanyRole.class, orgCompany.getIntValue("role")));

            List<Attribute<Integer>> members = new ArrayList<>();
            List<Integer> memberIds = orgCompany.getList("members", Integer.class);
            memberIds.forEach(id -> {
                Attribute<Integer> attribute = DataFinder.findAttributeByValue(id, allCompanies);
                if (attribute != null) {
                    members.add(attribute);
                }
            });

            company.put("members", members);
            companies.add(company);
        }

        return companies;
    }

    public static JSONArray getPersonnel(String json) {
        JSONArray orgPersonnel = JSON.parseArray(json);
        if(orgPersonnel.isEmpty()) return new JSONArray();
        List<Attribute<Integer>> allPersonnel = getAttributesForRedis(EntryCategory.PERSONNEL);
        List<Attribute<Integer>> allRole = getAttributesForRedis(EntryCategory.ROLE);

        JSONArray personnel = new JSONArray();
        for (int i = 0; i < orgPersonnel.size(); i++) {
            JSONObject orgItem = orgPersonnel.getJSONObject(i);
            JSONObject newItem = new JSONObject();

            if(orgItem.getIntValue("main") == 1) {
                newItem.put("main", 1);
            }

            Attribute<Integer> role = DataFinder.findAttributeByValue(orgItem.getIntValue("role"), allRole);
            newItem.put("role", role != null ? role : new Attribute<>("default", 0));

            List<Attribute<Integer>> members = new ArrayList<>();
            List<Integer> memberIds = orgItem.getList("members", Integer.class);
            memberIds.forEach(id -> {
                Attribute<Integer> attribute = DataFinder.findAttributeByValue(id, allPersonnel);
                if (attribute != null) {
                    members.add(attribute);
                }
            });

            newItem.put("members", members);
            personnel.add(newItem);
        }

        return personnel;
    }

    public static JSONArray getSpecs(String json) {
        JSONArray specs = JSON.parseArray(json);
        if(specs.isEmpty()) return new JSONArray();
        List<Attribute<Integer>> allSpecParameter = getAttributesForRedis(EntryCategory.SPEC_PARAM);
        for (int i = 0; i < specs.size(); i++) {
            JSONObject item = specs.getJSONObject(i);

            item.put("label", DataFinder.findAttributeByValue(item.getIntValue("label"), allSpecParameter));
        }

        return specs;
    }

    public static List<Attribute<Integer>> getSerials(String json) {

        List<Attribute<Integer>> serials = new ArrayList<>();

        int[] ids = JSON.parseObject(json, int[].class);

        if(ids.length == 0) return serials;

        List<Attribute<Integer>> allPublications = getAttributesForRedis(EntryCategory.PUBLICATION);

        serials.addAll(DataFinder.findAttributesByValues(ids, allPublications));

        return serials;
    }

    public static List<Attribute<Integer>> getClassifications(String json) {

        List<Attribute<Integer>> classifications = new ArrayList<>();

        int[] ids = JSON.parseObject(json, int[].class);
        List<Attribute<Integer>> allClassifications = getAttributesForRedis(EntryCategory.CLASSIFICATION);

        for(int id : ids) {
            classifications.add(DataFinder.findAttributeByValue(id, allClassifications));
        }

        return classifications;
    }

    public static List<Attribute<Integer>> getFranchises(String json) {

        List<Attribute<Integer>> franchises = new ArrayList<>();

        int[] ids = JSON.parseObject(json, int[].class);
        List<Attribute<Integer>> allFranchises = getAttributesForRedis(EntryCategory.FRANCHISE);

        for(int id : ids) {
            franchises.add(DataFinder.findAttributeByValue(id, allFranchises));
        }

        return franchises;
    }

    public static Attribute<Integer> getFranchise(int id) {

        List<Attribute<Integer>> allFranchises = getAttributesForRedis(EntryCategory.FRANCHISE);

        return DataFinder.findAttributeByValue(id, allFranchises);
    }

    @SuppressWarnings("unchecked")
    public static List<Attribute<Integer>> getAttributesForRedis(EntryCategory category) {
        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");

        List<Attribute<Integer>> attributes = new ArrayList<>();

        String key = category.getLocaleKey();

        Object res = redisUtil.get(key);
        if(res != null) {
            attributes.addAll((List<Attribute<Integer>>) redisUtil.get(key));
        }

        return attributes;
    }

}
