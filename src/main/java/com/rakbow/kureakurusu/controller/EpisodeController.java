package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.dto.EpisodeDeleteCmd;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.common.File;
import com.rakbow.kureakurusu.service.EpisodeService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/11 15:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/ep")
public class EpisodeController {

    private final EpisodeService srv;

    @PostMapping("upload-file")
    public ApiResult uploadFile(int id, MultipartFile[] files, List<File> fileInfos) {
        srv.updateFile(id, files, fileInfos);
        return new ApiResult().ok(I18nHelper.getMessage("file.update.success"));
    }

    @PostMapping("delete-file")
    public ApiResult deleteFile(@RequestBody EpisodeDeleteCmd cmd) {
        srv.deleteFiles(cmd.getId(), cmd.getFiles());
        return new ApiResult().ok(I18nHelper.getMessage("file.delete.success"));
    }

}
