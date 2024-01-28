package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.service.EntityService;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;

/**
 * @author Rakbow
 * @since 2023-02-12 16:38
 */
@Controller
@RequestMapping("/db")
public class EntityController {

    //region ------引入实例------

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private UserService userService;
    @Resource
    private EntityService entityService;
    @Resource
    private RedisUtil redisUtil;
    

    //endregion

//    @RequestMapping(path = "/get-entity-amount-info", method = RequestMethod.GET)
//    @ResponseBody
//    public String getEntityAmountInfo() {
//        ApiResult res = new ApiResult();
//        try {
//            res.data = entityService.getItemAmount();
//        } catch (Exception e) {
//            res.setErrorMessage(e);
//        }
//        return res.toJson();
//    }

    //region search

    @RequestMapping(path = "/simple-search", method = RequestMethod.POST)
    @ResponseBody
    public String simpleSearch(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            String keyword = json.getString("keyword");
            int entityType = json.getInteger("entityType");
            int offset = json.getInteger("offset");
            int limit = json.getInteger("limit");
            res.data = entityService.simpleSearch(keyword, entityType, offset, limit);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

    //region common update item info

    //update item specs
    @RequestMapping(path = "/update-item-specs", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemSpec(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = json.getInteger("entityId");
            String tableName = Entity.getTableName(json.getIntValue("entityType"));
            String spec = json.getString("spec");
            entityService.updateItemSpecs(tableName, entityId, spec);
            res.message = I18nHelper.getMessage("entity.crud.spec.update.success");
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

}
