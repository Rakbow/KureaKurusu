package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.service.EpisodeService;
import com.rakbow.kureakurusu.util.I18nHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/11 15:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/ep")
public class EpisodeController {

    private static final Logger log = LoggerFactory.getLogger(EpisodeController.class);
    private final EpisodeService service;

    @PostMapping("upload-file")
    public ApiResult uploadFile(int id, MultipartFile[] files, List<File> fileInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (files == null || files.length == 0) {
                throw new Exception(I18nHelper.getMessage("file.empty"));
            }
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

}
