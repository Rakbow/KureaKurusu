package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.ImageUpdateCmd;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.service.ResourceService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/26 20:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db")
public class ResourceController {

    private final ResourceService srv;

    //region image

    @PostMapping("get-images")
    public ApiResult getEntityImages(@RequestBody EntityQry qry) {
        return new ApiResult().load(srv.getEntityImages(qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("add-images")
    public ApiResult addEntityImages(int entityType, long entityId, MultipartFile[] files, String infos) {
        //check
        if (files == null || files.length == 0) return new ApiResult().fail(I18nHelper.getMessage("file.empty"));
        //save
        List<Image> images = JsonUtil.toJavaList(infos, Image.class);
        srv.addEntityImage(entityType, entityId, files, images);
        return new ApiResult().ok(I18nHelper.getMessage("image.insert.success"));
    }

    @PostMapping("update-images")
    public ApiResult updateEntityImages(@RequestBody ImageUpdateCmd cmd) {
        ApiResult res = new ApiResult();
        List<Image> images = cmd.getImages();
        if (images.isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        //update
        if (cmd.update()) {
            //save
            srv.updateEntityImage(images);
            res.ok(I18nHelper.getMessage("image.update.success"));
        }//delete
        else if (cmd.delete()) {
            srv.deleteEntityImage(images);
            res.ok(I18nHelper.getMessage("image.delete.success"));
        } else {
            return res.fail(I18nHelper.getMessage("entity.error.not_action"));
        }
        return res;
    }

    //endregion

}
