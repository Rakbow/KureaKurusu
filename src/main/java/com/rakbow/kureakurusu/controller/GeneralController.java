package com.rakbow.kureakurusu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.GetOptionQry;
import com.rakbow.kureakurusu.data.dto.GeneralSearchQry;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.dto.ImageUpdateCmd;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.ExcelUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
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
    private final ItemService itemSrv;
    private final ProductService productSrv;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final EntityUtil entityUtil;
    //endregion

    //region common

    @PostMapping("statistic-info")
    public ApiResult getStatisticInfo() {
        return new ApiResult();
    }

    @PostMapping("search")
    public ApiResult searchItem(@RequestBody GeneralSearchQry qry) {
        ApiResult res = new ApiResult();
        if(qry.getEntityType() == EntityType.ITEM.getValue())
            res.load(itemSrv.search(new SimpleSearchParam(qry.getParam())));
        else if(qry.getEntityType() == EntityType.PRODUCT.getValue())
            res.load(productSrv.searchProducts(qry.getParam()));
        return res;
    }

    @PostMapping("get-option")
    public ApiResult getOption(@RequestBody GetOptionQry qry) {
        return new ApiResult().load(entityUtil.getDetailOptions(qry.getType()));
    }

    @PostMapping("update-entry-status")
    public ApiResult updateEntryStatus(@RequestBody UpdateStatusDTO dto) {
        srv.updateEntryStatus(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.status.update.success"));
    }

    @PostMapping("update-entry-detail")
    public ApiResult updateEntryDetail(@RequestBody UpdateDetailDTO dto) {
        srv.updateEntryDetail(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.description.update.success"));
    }

    //endregion

    //region image

    @PostMapping("get-images")
    public ApiResult getEntryImages(@RequestBody EntityQry qry) {
        return new ApiResult().load(srv.getEntryImages(qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("add-images")
    public ApiResult addEntryImages(int entityType, int entityId, MultipartFile[] images, String imageInfos) {
        //check
        if (images == null || images.length == 0) return new ApiResult().fail(I18nHelper.getMessage("file.empty"));
        //save
        srv.addEntryImages(entityType, entityId, images, imageInfos);
        return new ApiResult().ok(I18nHelper.getMessage("image.insert.success"));
    }

    @PostMapping("update-images")
    public ApiResult updateEntryImages(@RequestBody ImageUpdateCmd cmd) {
        ApiResult res = new ApiResult();
        List<Image> images = cmd.getImages();
        if(images.isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        //update
        if (cmd.update()) {
            //check
            CommonImageUtil.checkUpdateImages(images);
            //save
            srv.updateEntryImages(cmd.getEntityType(), cmd.getEntityId(), images);
            res.ok(I18nHelper.getMessage("image.update.success"));
        }//delete
        else if (cmd.delete()) {
            srv.deleteEntryImages(cmd.getEntityType(), cmd.getEntityId(), images);
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
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.refresh.success"));
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
