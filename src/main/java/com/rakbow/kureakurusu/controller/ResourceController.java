package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.ImageListParams;
import com.rakbow.kureakurusu.data.dto.ImageUpdateDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.service.ResourceService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import io.github.linpeilie.Converter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    private final Converter converter;

    //region image

    @PostMapping("get-images")
    public ApiResult getEntityImages(@RequestBody ListQueryDTO dto) {
        return new ApiResult().load(
                srv.getEntityImages(new ImageListParams(dto))
        );
    }

    @PostMapping("add-image")
    public ApiResult addEntityImage(int entityType, long entityId, MultipartFile[] files, String infos) {
        //check
        if (files == null || files.length == 0) return new ApiResult().fail(I18nHelper.getMessage("file.empty"));
        //save
        List<Image> images = JsonUtil.toJavaList(infos, Image.class);
        srv.addEntityImage(entityType, entityId, files, images);
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

}
