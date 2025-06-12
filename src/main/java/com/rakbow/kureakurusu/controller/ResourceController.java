package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.service.ResourceService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import io.github.linpeilie.Converter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private final Converter converter;

    //region image

    @PostMapping("get-images")
    public ApiResult getEntityImages(@RequestBody ListQuery dto) {
        return new ApiResult().load(
                srv.getEntityImages(new ImageListQueryDTO(dto))
        );
    }

    @PostMapping("get-display-images")
    public ApiResult getEntityImages(@RequestBody EntityQry qry) {
        return new ApiResult().load(srv.getEntityDisplayImages(qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("upload-image")
    public ApiResult addEntityImage(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") int entityId,
            @RequestParam("infos") String infoStr,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("generateThumb") boolean generateThumb
    ) {
        List<ImageCreateDTO> infos = JsonUtil.toJavaList(infoStr, ImageCreateDTO.class);
        List<ImageMiniDTO> images = new ArrayList<>();
        for(int i = 0;i<infos.size();i++) {
            images.add(new ImageMiniDTO(infos.get(i), files.get(i)));
        }
        //check
        if (images.isEmpty()) return new ApiResult().fail(I18nHelper.getMessage("file.empty"));
        //save
        srv.uploadEntityImage(entityType, entityId, images, generateThumb);
        return new ApiResult().ok(I18nHelper.getMessage("image.insert.success"));
    }

    @PostMapping("update-image")
    public ApiResult updateEntityImage(@Valid @RequestBody ImageUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        srv.updateEntityImage(converter.convert(dto, Image.class));
        return new ApiResult().ok(I18nHelper.getMessage("image.update.success"));
    }

    @DeleteMapping("delete-images")
    public ApiResult deleteEntityImages(@RequestBody List<Image> images) {
        srv.deleteEntityImage(images);
        return new ApiResult().ok(I18nHelper.getMessage("image.delete.success"));
    }

    //endregion

    @PostMapping("file/list")
    public ApiResult fileList(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.getFileList(dto));
    }

    @PostMapping("file/update")
    public ApiResult updateFile(@Valid @RequestBody FileUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        return new ApiResult().ok(srv.updateFile(dto));
    }

    @PostMapping("file/upload")
    public ApiResult uploadFiles(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") long entityId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("names") List<String> names,
            @RequestParam("remarks") List<String> remarks
    ) {
        return new ApiResult().ok(srv.uploadFiles(entityType, entityId, files, names, remarks));
    }

}
