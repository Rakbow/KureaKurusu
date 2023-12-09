package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.data.ActionResult;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.MetaData;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 4:40
 * @Description:
 */
@Controller
@RequestMapping("/db")
public class GeneralController {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

    @Resource
    private GeneralService service;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //region common
    @RequestMapping(path = "/get-meta-data", method = RequestMethod.POST)
    @ResponseBody
    public String getMetaData() {
        ApiResult res = new ApiResult();
        try {
            res.data = MetaData.getOptions();
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(value = "/update-items-status", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemsStatus(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(json.getIntValue("entity"));
            List<Integer> ids = json.getList("ids", Integer.class);
            boolean status = json.getBoolean("status");
            service.updateItemStatus(tableName, ids, status?1:0);
            res.message = I18nHelper.getMessage("entity.crud.status.update.success");
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(path = "/update-item-detail", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemsDetail(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int id = json.getInteger("entityId");
            String tableName = Entity.getTableName(json.getIntValue("entityType"));
            String text = json.getString("text");
            service.updateItemDetail(tableName, id, text);
            res.message = I18nHelper.getMessage("entity.crud.description.update.success");
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

    //region image

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0)
                throw new Exception(I18nHelper.getMessage("file.empty"));

            String entityName = Entity.getTableName(entityType);

            //原始图片信息json数组
            List<Image> originalImages = service.getItemImages(entityName, entityId);
            //新增图片的信息
            List<Image> newImageInfos = JSON.parseArray(imageInfos).toJavaList(Image.class);

            //检测数据合法性
            CommonImageUtil.checkAddImages(newImageInfos, originalImages);

            ActionResult ar = service.addItemImages(entityName, entityId, images, originalImages, newImageInfos);
            if(!ar.state) throw new Exception(ar.message);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //更新图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemImages(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            //获取图书id
            int entityId = json.getInteger("entityId");
            int action = json.getInteger("action");
            String tableName = Entity.getTableName(json.getInteger("entityType"));

            JSONArray images = json.getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
                images.getJSONObject(i).remove("thumbUrl50");
            }

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMsg = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.isBlank(errorMsg)) {
                    throw new Exception(errorMsg);
                }

                res.message = service.updateItemImages(tableName, entityId, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = service.deleteItemImages(tableName, entityId, images);
            }else {
                throw new Exception(I18nHelper.getMessage("entity.error.not_action"));
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

    //region other

    //like
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String likeEntity(@RequestBody JSONObject json, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            int entityType = json.getIntValue("entity");
            int entityId = json.getIntValue("entityId");

            // 从cookie中获取点赞token
            String likeToken = TokenInterceptor.getLikeToken();
            if(likeToken == null) {
                //生成likeToken,并返回
                likeToken = CommonUtil.generateUUID();
                Cookie cookie = new Cookie("like_token", likeToken);
                cookie.setPath(contextPath);
                response.addCookie(cookie);
            }
            if(service.entityLike(entityType, entityId, likeToken)) {
                res.message = I18nHelper.getMessage("entity.like.success");
            }else {
                throw new Exception(I18nHelper.getMessage("entity.like.failed"));
            }
        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion
}
