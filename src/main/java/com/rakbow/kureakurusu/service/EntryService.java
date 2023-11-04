package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.entity.Entry;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-03 2:09
 * @Description:
 */
@Service
public class EntryService {

    @Resource
    private EntryMapper entryMapper;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private I18nService i18n;

    //region CURD

    /**
     * 新增
     *
     * @param entry 新增
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String addEntry(Entry entry) {
        entryMapper.addEntry(entry);
        return i18n.getMessage("entity.curd.insert.success", Entity.ENTRY.getNameZh());
    }

    /**
     * 根据Id获取
     *
     * @param id id
     * @return Entry
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Entry getEntry(int id) {
        return entryMapper.getEntry(id);
    }

    /**
     * 根据Id获取
     *
     * @param ids ids
     * @return Entry
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Entry> getEntries(List<Integer> ids) {
        return entryMapper.getEntries(ids);
    }

    /**
     * 根据Id删除
     *
     * @param entry Entry
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteEntry(Entry entry) {
        //删除前先把服务器上对应图片全部删除
        qiniuFileUtil.commonDeleteAllFiles(JSON.parseArray(entry.getImages()));
        //删除专辑
        entryMapper.deleteEntry(entry.getId());
        visitUtil.deleteVisit(Entity.ENTRY.getId(), entry.getId());
    }

    /**
     * 更新基础信息
     *
     * @param id id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateEntry(int id, Entry entry) {
        entryMapper.updateEntry(id, entry);
        return i18n.getMessage("entity.curd.update.success", Entity.ENTRY.getNameZh());
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getEntriesByFilter(QueryParams param) {

        JSONObject filter = param.getFilters();

        String name = filter.getJSONObject("name").getString("value");
        String nameZh = filter.getJSONObject("nameZh").getString("value");
        String nameEn = filter.getJSONObject("nameEn").getString("value");
        int category = 100;
        if (filter.getJSONObject("category").get("value") != null) {
            category = filter.getJSONObject("category").getIntValue("value");
        }

        List<Entry> entries  = new ArrayList<>();
        // List<Entry> entries = entryMapper.getEntriesByFilter(name, nameZh, nameEn, category,
        //         param.getSortField(), param.getSortOrder(),  param.getFirst(), param.getRows());

        int total = entryMapper.getEntriesRowsByFilter(name, nameZh, nameEn, category);

        return new SearchResult(entries, total);
    }

    /**
     * 检测数据合法性
     *
     * @param json 专辑json
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkEntryJson(JSONObject json) {
        if (StringUtils.isBlank(json.getString("name"))) {
            return i18n.getMessage("entity.crud.name.required_field");
        }
        if (StringUtils.isBlank(json.getString("nameZh"))) {
            return i18n.getMessage("entity.crud.name_zh.required_field");
        }
        if (StringUtils.isBlank(json.getString("nameEn"))) {
            return i18n.getMessage("entity.crud.name_en.required_field");
        }
        if (StringUtils.isBlank(json.getString("category"))) {
            return i18n.getMessage("entity.crud.category.required_field");
        }
        return "";
    }

    //endregion

    /**
     * 刷新Redis缓存中的Entry数据
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public void refreshRedisEntries(int category) {

        List<Entry> entries = entryMapper.getEntryByCategory(category);

        JSONArray entriesZh = new JSONArray();
        entries.forEach(entry -> entriesZh.add(new Attribute<Integer>(entry.getNameZh(), entry.getId())));
        JSONArray entriesEn = new JSONArray();
        entries.forEach(entry -> entriesEn.add(new Attribute<Integer>(entry.getNameEn(), entry.getId())));

        redisUtil.set(EntryCategory.getZhRedisKeyById(category), entriesZh);
        redisUtil.set(EntryCategory.getEnRedisKeyById(category), entriesEn);
    }

}
