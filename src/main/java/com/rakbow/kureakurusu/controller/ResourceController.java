package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.service.ResourceService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import io.github.linpeilie.Converter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("add-image")
    public ApiResult addEntityImage(@RequestBody ImageCreateDTO dto) {
        //check
        if (dto.getImages().isEmpty()) return new ApiResult().fail(I18nHelper.getMessage("file.empty"));
        //save
        srv.addEntityImage(dto.getEntityType(), dto.getEntityId(), dto.getImages(), false);
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
