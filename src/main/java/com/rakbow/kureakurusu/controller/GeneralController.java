package com.rakbow.kureakurusu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.GetOptionQry;
import com.rakbow.kureakurusu.data.dto.GeneralSearchQry;
import com.rakbow.kureakurusu.data.dto.UpdateDetailCmd;
import com.rakbow.kureakurusu.data.dto.UpdateStatusCmd;
import com.rakbow.kureakurusu.data.dto.ImageUpdateCmd;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.BookService;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.service.ProductService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.ExcelUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-06 4:40
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db")
public class GeneralController {

    //region inject
    private final GeneralService srv;
    private final AlbumService albumSrv;
    private final BookService bookSrv;
    private final ProductService productSrv;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final EntityUtil entityUtil;
    //endregion

    //region common

    @PostMapping("statistic-info")
    public ApiResult getStatisticInfo() {
        return new ApiResult().load(srv.getStatisticInfo());
    }

    @PostMapping("search")
    public ApiResult searchItem(@RequestBody GeneralSearchQry qry) {
        ApiResult res = new ApiResult();
        if(qry.getEntityType() == Entity.ALBUM.getValue())
            res.load(albumSrv.searchAlbums(qry.getParam()));
        else if(qry.getEntityType() == Entity.BOOK.getValue())
            res.load(bookSrv.searchBooks(qry.getParam()));
        else if(qry.getEntityType() == Entity.PRODUCT.getValue())
            res.load(productSrv.searchProducts(qry.getParam()));
        return res;
    }

    @PostMapping("get-option")
    public ApiResult getOption(@RequestBody GetOptionQry qry) {
        return new ApiResult().load(entityUtil.getDetailOptions(qry.getEntityType()));
    }

    @PostMapping("update-items-status")
    public ApiResult updateItemsStatus(@RequestBody UpdateStatusCmd cmd) {
        srv.updateItemStatus(cmd);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.status.update.success"));
    }

    @PostMapping("update-item-detail")
    public ApiResult updateItemDetail(@RequestBody UpdateDetailCmd cmd) {
        srv.updateItemDetail(cmd);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.description.update.success"));
    }

    @PostMapping("update-item-bonus")
    public ApiResult updateItemBonus(@RequestBody UpdateDetailCmd cmd) {
        srv.updateItemBonus(cmd);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.bonus.update.success"));
    }

    //endregion

    //region image

    @PostMapping("get-images")
    public ApiResult getItemImages(@RequestBody EntityQry qry) {
        String tableName = Entity.getTableName(qry.getEntityType());
        return new ApiResult().load(srv.getItemImages(tableName, qry.getEntityId()));
    }

    @PostMapping("add-images")
    public ApiResult addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos) {
        //check
        if (images == null || images.length == 0) return new ApiResult().fail(I18nHelper.getMessage("file.empty"));
        //save
        srv.addItemImages(entityType, entityId, images, imageInfos);
        return new ApiResult().ok(I18nHelper.getMessage("image.insert.success"));
    }

    @PostMapping("update-images")
    public ApiResult updateItemImages(@RequestBody ImageUpdateCmd cmd) {
        ApiResult res = new ApiResult();
        List<Image> images = cmd.getImages();
        if(images.isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        String tableName = Entity.getTableName(cmd.getEntityType());
        //update
        if (cmd.update()) {
            //check
            CommonImageUtil.checkUpdateImages(images);
            //save
            srv.updateItemImages(tableName, cmd.getEntityId(), images);
            res.ok(I18nHelper.getMessage("image.update.success"));
        }//delete
        else if (cmd.delete()) {
            srv.deleteItemImages(tableName, cmd.getEntityId(), images);
            res.ok(I18nHelper.getMessage("image.delete.success"));
        }else {
            return res.fail(I18nHelper.getMessage("entity.error.not_action"));
        }
        return res;
    }

    //endregion

    //region person role

    @PostMapping("refresh-person-role")
    public ApiResult refreshPersonRole() {
        srv.refreshPersonRoleSet();
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.refresh.success", Entity.ROLE.getName()));
    }

    //endregion

    //region other

    @PostMapping("like")
    public ApiResult like(@RequestBody EntityQry qry, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        //get like token from cookie
        String likeToken = TokenInterceptor.getLikeToken();
        if(StringUtils.isBlank(likeToken)) {
            //generate like token and return
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
        return res;
    }

    //endregion

    //region import

    @SneakyThrows
    @PostMapping("import-entity")
    public ApiResult importEntity(MultipartFile file) {
        ApiResult res = new ApiResult();
        if(file.isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        List<Person> items = ExcelUtil.getDataFromExcel(file.getInputStream(), Person.class);
        Db.saveBatch(items);
        return res;
    }

    //endregion
}
