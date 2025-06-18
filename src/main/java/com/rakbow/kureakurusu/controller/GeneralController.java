package com.rakbow.kureakurusu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.ExcelUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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

    @Value("${server.servlet.context-path}")
    private String contextPath;
    //endregion

    //region common

    @PostMapping("statistic-info")
    public ApiResult getStatisticInfo() {
        return new ApiResult();
    }

    @GetMapping("get-options")
    public ApiResult getOption() {
        return new ApiResult().load(srv.getOptions());
    }

    @PostMapping("update-entity-status")
    public ApiResult updateEntityStatus(@RequestBody UpdateStatusDTO dto) {
        srv.updateEntityStatus(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.status.update.success"));
    }

    @PostMapping("update-entity-detail")
    public ApiResult updateEntityDetail(@RequestBody UpdateDetailDTO dto) {
        srv.updateEntityDetail(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.description.update.success"));
    }

    //endregion

    //region person role

    @PostMapping("refresh-role")
    public ApiResult refreshPersonRole() {
        srv.refreshRoleSet();
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.refresh.success"));
    }

    //endregion

    //region other

    @PostMapping("like")
    public ApiResult like(@RequestBody EntityQry qry, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        //get like token from cookie
        String likeToken = TokenInterceptor.getLikeToken();
        if (StringUtils.isBlank(likeToken)) {
            //generate like token and return
            likeToken = CommonUtil.generateUUID(0);
            Cookie cookie = new Cookie("like_token", likeToken);
            cookie.setPath(contextPath);
            response.addCookie(cookie);
        }
        if (srv.like(qry.getEntityType(), qry.getEntityId(), likeToken)) {
            res.ok(I18nHelper.getMessage("entity.like.success"));
        } else {
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
        if (file.isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        List<Entry> items = ExcelUtil.getDataFromExcel(file.getInputStream(), Entry.class);
        Db.saveBatch(items);
        return res;
    }

    //endregion
}
