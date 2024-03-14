package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.GetOptionQry;
import com.rakbow.kureakurusu.data.dto.base.GeneralSearchQry;
import com.rakbow.kureakurusu.data.dto.common.UpdateDetailCmd;
import com.rakbow.kureakurusu.data.dto.common.UpdateStatusCmd;
import com.rakbow.kureakurusu.data.dto.image.ImageUpdateCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.BookService;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.service.ProductService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    private final GeneralService srv;
    private final AlbumService albumSrv;
    private final BookService bookSrv;
    private final ProductService productSrv;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final EntityUtil entityUtil;

    //region common

    @PostMapping("statistic-info")
    public ApiResult getStatisticInfo() {
        ApiResult res = new ApiResult();
        try {
            res.loadData(srv.getStatisticInfo());
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("search")
    public ApiResult searchItem(@RequestBody GeneralSearchQry qry) {
        ApiResult res = new ApiResult();
        try {
            if(qry.getEntityType() == Entity.ALBUM.getValue())
                res.loadData(albumSrv.searchAlbums(qry.getParam()));
            else if(qry.getEntityType() == Entity.BOOK.getValue())
                res.loadData(bookSrv.searchBooks(qry.getParam()));
            else if(qry.getEntityType() == Entity.PRODUCT.getValue())
                res.loadData(productSrv.searchProducts(qry.getParam()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("get-option")
    public ApiResult getOption(@RequestBody GetOptionQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.loadData(entityUtil.getDetailOptions(qry.getEntityType()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("update-items-status")
    public ApiResult updateItemsStatus(@RequestBody UpdateStatusCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(cmd.getEntity());
            srv.updateItemStatus(tableName, cmd.getIds(), cmd.status());
            res.ok(I18nHelper.getMessage("entity.crud.status.update.success"));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("update-item-detail")
    public ApiResult updateItemDetail(@RequestBody UpdateDetailCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(cmd.getEntityType());
            srv.updateItemDetail(tableName, cmd.getEntityId(), cmd.getText());
            res.ok(I18nHelper.getMessage("entity.crud.description.update.success"));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("update-item-bonus")
    public ApiResult updateItemBonus(@RequestBody UpdateDetailCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(cmd.getEntityType());
            srv.updateItemBonus(tableName, cmd.getEntityId(), cmd.getText());
            res.ok(I18nHelper.getMessage("entity.crud.bonus.update.success"));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

    //region image

    @PostMapping("get-images")
    public ApiResult getItemImages(@RequestBody EntityQry qry) {
        ApiResult res = new ApiResult();
        try {
            String tableName = Entity.getTableName(qry.getEntityType());
            res.loadData(srv.getItemImages(tableName, qry.getEntityId()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //新增图片
    @PostMapping("add-images")
    public ApiResult addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (images == null || images.length == 0)
                throw new Exception(I18nHelper.getMessage("file.empty"));
            //save
            srv.addItemImages(entityType, entityId, images, imageInfos);
            res.ok(I18nHelper.getMessage("image.insert.success"));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //更新图片，删除或更改信息
    @PostMapping("update-images")
    public ApiResult updateItemImages(@RequestBody ImageUpdateCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            List<Image> images = cmd.getImages();
            if(images.isEmpty())
                throw new Exception(I18nHelper.getMessage("file.empty"));
            String tableName = Entity.getTableName(cmd.getEntityType());
            //更新图片信息
            if (cmd.update()) {
                //检测是否存在多张封面
                CommonImageUtil.checkUpdateImages(images);
                //save
                srv.updateItemImages(tableName, cmd.getEntityId(), images);
                res.ok(I18nHelper.getMessage("image.update.success"));
            }//删除图片
            else if (cmd.delete()) {
                srv.deleteItemImages(tableName, cmd.getEntityId(), images);
                res.ok(I18nHelper.getMessage("image.delete.success"));
            }else {
                throw new Exception(I18nHelper.getMessage("entity.error.not_action"));
            }
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

    //region person role

    @PostMapping("refresh-person-role")
    public ApiResult refreshPersonRole() {
        ApiResult res = new ApiResult();
        try {
            srv.refreshPersonRoleSet();
            res.ok(I18nHelper.getMessage("entity.curd.refresh.success", Entity.ROLE.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

    //region other

    //like
    @PostMapping("like")
    public ApiResult like(@RequestBody EntityQry qry, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            // 从cookie中获取点赞token
            String likeToken = TokenInterceptor.getLikeToken();
            if(StringUtils.isBlank(likeToken)) {
                //生成likeToken,并返回
                likeToken = CommonUtil.generateUUID();
                Cookie cookie = new Cookie("like_token", likeToken);
                cookie.setPath(contextPath);
                response.addCookie(cookie);
            }
            if(srv.like(qry.getEntityType(), qry.getEntityId(), likeToken)) {
                res.ok(I18nHelper.getMessage("entity.like.success"));
            }else {
                res.fail(I18nHelper.getMessage("entity.like.failed"));
            }
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion
}
