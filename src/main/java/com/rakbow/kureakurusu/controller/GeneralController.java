package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.data.ActionResult;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.dto.common.UpdateDetailCmd;
import com.rakbow.kureakurusu.data.dto.common.UpdateStatusCmd;
import com.rakbow.kureakurusu.data.dto.image.ImageUpdateCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-06 4:40
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db")
public class GeneralController {

    private static final Logger log = LoggerFactory.getLogger(GeneralController.class);
    private final GeneralService service;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //region common
    @PostMapping("get-meta-data")
    public ApiResult getMetaData() {
        ApiResult res = new ApiResult();
        try {
            res.data = MetaData.getOptions();
        } catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    @PostMapping("update-items-status")
    public ApiResult updateItemsStatus(@RequestBody UpdateStatusCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(cmd.getEntity());
            service.updateItemStatus(tableName, cmd.getIds(), cmd.status());
            res.ok(I18nHelper.getMessage("entity.crud.status.update.success"));
        } catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    @PostMapping("update-item-detail")
    public ApiResult updateItemsDetail(@RequestBody UpdateDetailCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(cmd.getEntityType());
            service.updateItemDetail(tableName, cmd.getEntityId(), cmd.getText());
            res.ok(I18nHelper.getMessage("entity.crud.description.update.success"));
        } catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    //endregion

    //region image

    //新增图片
    @PostMapping("add-images")
    public ApiResult addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (images == null || images.length == 0)
                throw new Exception(I18nHelper.getMessage("file.empty"));

            String tableName = Entity.getTableName(entityType);
            //原始图片信息json数组
            List<Image> originalImages = service.getItemImages(tableName, entityId);
            //新增图片的信息
            List<Image> newImageInfos = JsonUtil.toJavaList(imageInfos, Image.class);
            //检测数据合法性
            CommonImageUtil.checkAddImages(newImageInfos, originalImages);
            //save
            ActionResult ar = service.addItemImages(tableName, entityId, images, originalImages, newImageInfos);
            if(!ar.state) throw new Exception(ar.message);
        } catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    //更新图片，删除或更改信息
    @PostMapping("update-images")
    public ApiResult updateItemImages(@RequestBody ImageUpdateCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(cmd.getEntityType());
            List<Image> images = cmd.getImages();
            //更新图片信息
            if (cmd.update()) {
                //检测是否存在多张封面
                CommonImageUtil.checkUpdateImages(images);
                //save
                service.updateItemImages(tableName, cmd.getEntityId(), images);
                res.ok(I18nHelper.getMessage("image.update.success"));
            }//删除图片
            else if (cmd.delete()) {
                service.deleteItemImages(tableName, cmd.getEntityId(), images);
                res.ok(I18nHelper.getMessage("image.delete.success"));
            }else {
                throw new Exception(I18nHelper.getMessage("entity.error.not_action"));
            }
        } catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    //endregion

    //region person role

    @RequestMapping(path = "/refresh-person-role", method = RequestMethod.POST)
    @ResponseBody
    public String refreshPersonRole() {
        ApiResult res = new ApiResult();
        try {
            service.refreshPersonRoleSet();
            res.message = I18nHelper.getMessage("entity.curd.refresh.success", Entity.ENTRY.getName());
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
