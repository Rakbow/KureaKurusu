package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import com.rakbow.kureakurusu.data.emun.system.UserAuthority;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.service.EntityService;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.util.common.*;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-12 16:38
 * @Description:
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

    //获取在线数据库首页
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getDatabasePage(Model model) {
        model.addAttribute("indexCoverUrl", redisUtil.get(RedisKey.INDEX_COVER_URL));
        return "database/database";
    }

    //region 获取数据库搜索主页
    @RequestMapping(path = "/albums", method = RequestMethod.GET)
    public String getAlbumIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/books", method = RequestMethod.GET)
    public String getBookIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/discs", method = RequestMethod.GET)
    public String getDiscIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public String getGameIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/merchs", method = RequestMethod.GET)
    public String getMerchIndexPage() {
        return "/database/database-index";
    }
    //endregion

    //region 获取数据库管理列表
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getDatabaseListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/entry", method = RequestMethod.GET)
    public String getEntryListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/album", method = RequestMethod.GET)
    public String getAlbumListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/book", method = RequestMethod.GET)
    public String getBookListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/disc", method = RequestMethod.GET)
    public String getDiscListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/game", method = RequestMethod.GET)
    public String getGameListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/merch", method = RequestMethod.GET)
    public String getMerchListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/product", method = RequestMethod.GET)
    public String getProductListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/franchise", method = RequestMethod.GET)
    public String getFranchiseListPage() {
        return "/database/database-list";
    }
    //endregion

    //region 获取index初始数据
    @RequestMapping(value = "/get-index-init-data", method = RequestMethod.POST)
    @ResponseBody
    public String getIndexInitData(@RequestBody JSONObject json, HttpServletRequest request) {
        int entityType = json.getIntValue("entityType");
        JSONObject initData = entityUtil.getDetailOptions(entityType);
        initData.put("justAddedItems", entityService.getJustAddedItems(entityType, 5));
        initData.put("popularItems", entityService.getPopularItems(entityType, 9));

        return initData.toJSONString();
    }

    //获取list初始数据
    @RequestMapping(value = "/get-list-init-data", method = RequestMethod.POST)
    @ResponseBody
    public String getListInitData(@RequestBody JSONObject json, HttpServletRequest request) {
        int entityType = json.getIntValue("entityType");
        JSONObject initData = entityUtil.getDetailOptions(entityType);
        initData.put("editAuth", UserAuthority.getUserOperationAuthority(AuthorityInterceptor.getCurrentUser()));
        return initData.toJSONString();
    }
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
//        return JSON.toJSONString(res);
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
        return JSON.toJSONString(res);
    }

    //endregion

    //region common update item info

    //update item status
    @RequestMapping(value = "/update-item-status", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemStatus(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(json.getIntValue("entityType"));
            int entityId = json.getIntValue("entityId");
            boolean status = json.getBoolean("status");
            entityService.updateItemStatus(tableName, entityId, status?1:0);
            res.message = ApiInfo.UPDATE_ITEM_STATUS_URL;
        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //update items status
    @RequestMapping(value = "/update-items-status", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemsStatus(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(json.getIntValue("entityType"));
            List<Integer> ids = json.getList("ids", Integer.class);
            boolean status = json.getBoolean("status");
            entityService.updateItemsStatus(tableName, ids, status?1:0);
            res.message = ApiInfo.UPDATE_ITEM_STATUS_URL;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //update item description
    @RequestMapping(path = "/update-item-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemsDescription(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = json.getInteger("entityId");
            String entityName = Entity.getTableName(json.getIntValue("entityType"));
            String description = json.get("text").toString();
            entityService.updateItemDescription(entityName, entityId, description);
            res.message = ApiInfo.UPDATE_DESCRIPTION_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //update item bonus
    @RequestMapping(path = "/update-item-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemBonus(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = json.getInteger("entityId");
            String tableName = Entity.getTableName(json.getIntValue("entityType"));
            String bonus = json.getString("text");
            entityService.updateItemBonus(tableName, entityId, bonus);
            res.message = ApiInfo.UPDATE_BONUS_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

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
            res.message = ApiInfo.UPDATE_SPEC_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //update item companies
    @RequestMapping(path = "/update-item-companies", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemCompanies(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = json.getInteger("entityId");
            String entityName = Entity.getTableName(json.getIntValue("entityType"));
            String companies = json.getString("companies");
            entityService.updateItemCompanies(entityName, entityId, companies);
            res.message = ApiInfo.UPDATE_COMPANIES_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //update item personnel
    @RequestMapping(path = "/update-item-personnel", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemPersonnel(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = json.getInteger("entityId");
            int entityType = json.getIntValue("entityType");
            String tableName = Entity.getTableName(entityType);
            String fieldName = "";
            if(entityType == Entity.ALBUM.getId()) {
                fieldName = "artists";
            }else if(entityType == Entity.BOOK.getId()) {
                fieldName = "personnel";
            }
            String personnel = json.getString("personnel");
            entityService.updateItemPersonnel(tableName, fieldName, entityId, personnel);
            res.message = ApiInfo.UPDATE_PERSONNEL_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region image

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) throw new Exception(ApiInfo.INPUT_FILE_EMPTY);

            String entityName = Entity.getTableName(entityType);

            //原始图片信息json数组
            List<Image> originalImages = entityService.getItemImages(entityName, entityId);
            //新增图片的信息
            List<Image> newImageInfos = JSON.parseArray(imageInfos).toJavaList(Image.class);

            //检测数据合法性
            CommonImageUtil.checkAddImages(newImageInfos, originalImages);

            ActionResult ar = entityService.addItemImages(entityName, entityId, images, originalImages, newImageInfos);
            if(!ar.state) throw new Exception(ar.message);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
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

                res.message = entityService.updateItemImages(tableName, entityId, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = entityService.deleteItemImages(tableName, entityId, images);
            }else {
                throw new Exception(ApiInfo.NOT_ACTION);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region other

    //like
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String likeEntity(@RequestBody JSONObject json, HttpServletRequest request, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            int entityType = json.getIntValue("entityType");
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
            if(entityService.entityLike(entityType, entityId, likeToken)) {
                res.message = ApiInfo.LIKE_SUCCESS;
            }else {
                throw new Exception(ApiInfo.LIKE_FAILED);
            }
        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
