package com.rakbow.kureakurusu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.EntityDTO;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.ExcelUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
        return ApiResult.ok(srv.getOptions());
    }

    @PostMapping("update-entity-status")
    public ApiResult updateEntityStatus(@RequestBody UpdateStatusDTO dto) {
        srv.updateEntityStatus(dto);
        return ApiResult.ok("entity.crud.status.update.success");
    }

    @PostMapping("update-entity-detail")
    public ApiResult updateEntityDetail(@RequestBody UpdateDetailDTO dto) {
        srv.updateEntityDetail(dto);
        return ApiResult.ok("entity.crud.detail.update.success");
    }

    //endregion

    //region other

    // @PostMapping("changelog-list")
    // public ApiResult changelogs(@RequestBody ChangelogListQueryDTO dto) {
    //     return ApiResult.ok(srv.changelogs(dto));
    // }

    @PostMapping("changelog-mini")
    public ApiResult changelog(@RequestBody EntityDTO dto) {
        return ApiResult.ok(srv.mini(dto.entityType(), dto.entityId()));
    }

    @PostMapping("like")
    public ApiResult like(@RequestBody EntityDTO dto, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        //get like token from cookie
        String likeToken = TokenInterceptor.getLikeToken();
        if (StringUtil.isBlank(likeToken)) {
            //generate like token and return
            likeToken = CommonUtil.generateUUID(0);
            Cookie cookie = new Cookie("like_token", likeToken);
            cookie.setPath(contextPath);
            response.addCookie(cookie);
        }
        if (srv.like(dto.entityType(), dto.entityId(), likeToken)) {
            return ApiResult.ok("entity.like.success");
        } else {
            throw new ApiException("entity.like.failed");
        }
    }

    //endregion

    //region other

    @SneakyThrows
    @PostMapping("import-entity")
    public ApiResult importEntity(MultipartFile file) {
        ApiResult res = new ApiResult();
        if (file.isEmpty()) throw ErrorFactory.fileEmpty();
        List<Entry> items = ExcelUtil.getDataFromExcel(file.getInputStream(), Entry.class);
        Db.saveBatch(items);
        return res;
    }

    @SneakyThrows
    @PostMapping("links")
    public ApiResult links(@RequestBody EntityDTO dto) {
        return ApiResult.ok(srv.links(dto.entityType(), dto.entityId()));
    }

    @SneakyThrows
    @PostMapping("local-path")
    public ApiResult localPath(@RequestBody EntityDTO dto) {
        srv.localPath(dto);
        return ApiResult.ok();
    }

    //endregion
}
