package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.EpisodeDeleteCmd;
import com.rakbow.kureakurusu.data.dto.ListQuery;
import com.rakbow.kureakurusu.service.EpisodeService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rakbow
 * @since 2024/1/11 15:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/ep")
public class EpisodeController {

    private final EpisodeService srv;

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(id));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.list(dto));
    }

    // @PostMapping("upload-file")
    // public ApiResult uploadFile(int id, MultipartFile[] files, List<File> fileInfos) {
    //     srv.updateFile(id, files, fileInfos);
    //     return new ApiResult().ok(I18nHelper.getMessage("file.update.success"));
    // }
    //
    // @PostMapping("delete-file")
    // public ApiResult deleteFile(@RequestBody EpisodeDeleteCmd cmd) {
    //     srv.deleteFiles(cmd.getId(), cmd.getFiles());
    //     return new ApiResult().ok(I18nHelper.getMessage("file.delete.success"));
    // }

}
