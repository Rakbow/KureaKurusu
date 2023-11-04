package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.FranchiseMapper;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.franchise.MetaInfo;
import com.rakbow.kureakurusu.entity.Franchise;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.RedisUtil;
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
 * @Create: 2022-08-20 1:17
 * @Description:
 */
@Service
public class FranchiseService {

    //region ------依赖注入------

    @Resource
    private FranchiseMapper franchiseMapper;
    @Resource
    private RedisUtil redisUtil;
    

    //endregion

    //region ------曾删改查------

//    //新增系列
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public String addFranchise(Franchise franchise){
//        int id = franchiseMapper.addFranchise(franchise);
//        return String.format(ApiInfo.INSERT_DATA_SUCCESS, Entity.FRANCHISE.getNameZh());
//    }

    //通过id查找系列
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Franchise getFranchise(int id){
        return franchiseMapper.getFranchise(id, true);
    }

    /**
     * 根据Id获取Franchise,需要判断权限
     *
     * @param id id
     * @return Franchise
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Franchise getFranchiseWithAuth(int id) {
        if(AuthorityInterceptor.isSenior()) {
            return franchiseMapper.getFranchise(id, true);
        }
        return franchiseMapper.getFranchise(id, false);
    }

    //修改系列信息
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public String updateFranchise(int id, Franchise franchise){
//        franchiseMapper.updateFranchise(id, franchise);
//        return String.format(ApiInfo.UPDATE_DATA_SUCCESS, Entity.FRANCHISE.getNameZh());
//    }

    //删除系列
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int deleteFranchise(int id){
        return franchiseMapper.deleteFranchise(id);
    }

    /**
     * 更新父级系列
     *
     * @param parentFranchiseId,childFranchiseIds 父系列id，子系列ids
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateParentFranchise(int parentFranchiseId, List<Integer> childFranchiseIds) {

        List<Franchise> originChildFranchises = franchiseMapper.getFranchisesByParentId(Integer.toString(parentFranchiseId));

        if(childFranchiseIds.isEmpty()) {
            //若子系列ids为空，则将原先父id为parentFranchiseId的所有系列父id归0
            originChildFranchises.forEach(franchise -> {
                MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
                metaInfo.metaId = "0";
                franchiseMapper.updateMetaInfo(franchise.getId(), JSON.toJSONString(metaInfo));
            });
        }else {
            //若子系列ids不为空，将父id不为该parentFranchiseId的系列父id归0
            originChildFranchises.forEach(franchise -> {
                if(!childFranchiseIds.contains(franchise.getId())) {
                    MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
                    metaInfo.metaId = "0";
                    franchiseMapper.updateMetaInfo(franchise.getId(), JSON.toJSONString(metaInfo));
                }
            });

            childFranchiseIds.forEach(id -> {
                Franchise franchise = getFranchise(id);
                MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
                metaInfo.metaId = Integer.toString(parentFranchiseId);
                franchiseMapper.updateMetaInfo(id, JSON.toJSONString(metaInfo));
            });
        }
    }

    //endregion

    //region ------数据处理------

    /**
     * 检测数据合法性
     *
     * @param franchiseJson franchiseJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkFranchiseJson(JSONObject franchiseJson) {
        if (StringUtils.isBlank(franchiseJson.getString("name"))) {
            return I18nHelper.getMessage("entity.crud.name.required_field");
        }

        if (StringUtils.isBlank(franchiseJson.getString("originDate"))) {
            return I18nHelper.getMessage("franchise.crud.origin_date.required_field");
        }
        return "";
    }

    /**
     * 处理前端传送数据
     *
     * @param franchiseJson franchiseJson
     * @return 处理后的 json格式数据
     * @author rakbow
     */
    public JSONObject handleFranchiseJson(JSONObject franchiseJson) {

        franchiseJson.put("originDate", franchiseJson.getDate("originDate"));

        JSONObject metaInfo = new JSONObject();
        if (franchiseJson.getBoolean("metaLabel")) {
            metaInfo.put("isMeta", "1");
        }else {
            metaInfo.put("isMeta", "0");
        }

        List<Integer> childFranchiseIds = franchiseJson.getList("childFranchises", Integer.class);

        metaInfo.put("childFranchises", childFranchiseIds);
        metaInfo.put("metaId", "0");
        franchiseJson.put("metaInfo", metaInfo);
        updateParentFranchise(franchiseJson.getIntValue("id"), childFranchiseIds);

        return franchiseJson;
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getFranchisesByFilter(QueryParams param) {

        JSONObject filter = param.getFilters();

        String name = filter.getJSONObject("name").getString("value");
        String nameZh = filter.getJSONObject("nameZh").getString("value");
        String isMeta;
        if (filter.getJSONObject("metaLabel").getBoolean("value") == null) {
            isMeta = null;
        }else {
            isMeta = filter.getJSONObject("metaLabel").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        List<Franchise> franchises  = new ArrayList<>();
        // List<Franchise> franchises = franchiseMapper.getFranchisesByFilter(name, nameZh, isMeta,
        //         AuthorityInterceptor.isSenior(), param.getSortField(), param.getSortOrder(), param.getFirst(), param.getRows());

        int total = franchiseMapper.getFranchisesRowsByFilter(name, nameZh, isMeta, AuthorityInterceptor.isSenior());

        return new SearchResult(franchises, total);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Franchise> getFranchisesByParentId(int parentId) {
        return franchiseMapper.getFranchisesByParentId(Integer.toString(parentId));
    }

    //endregion

}
