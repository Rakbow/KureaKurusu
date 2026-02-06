package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.service.ImageService;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
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
@RequestMapping("db/image")
public class ImageController {

    private final ImageService srv;

    //region image

    @PostMapping("list")
    public ApiResult list(@RequestBody ImageDTO.ImageListQueryDTO dto) {
        return ApiResult.ok(srv.list(dto));
    }

    @PostMapping("preview")
    public ApiResult preview(@RequestBody ImageDTO.ImagePreviewDTO dto) {
        return ApiResult.ok(srv.preview(dto));
    }

    @PostMapping("upload")
    public ApiResult upload(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") int entityId,
            @RequestParam("infos") String infoStr,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("generateThumb") boolean generateThumb
    ) {
        List<ImageDTO.ImageCreateDTO> infos = JsonUtil.toJavaList(infoStr, ImageDTO.ImageCreateDTO.class);
        List<ImageDTO.ImageMiniDTO> images = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            images.add(new ImageDTO.ImageMiniDTO(infos.get(i), files.get(i)));
        }
        //check
        if (images.isEmpty()) throw ErrorFactory.fileEmpty();
        //save
        srv.upload(entityType, entityId, images, generateThumb);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ImageDTO.ImageUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        srv.update(dto);
        return ApiResult.ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ImageDTO.ImageDeleteDTO dto) {
        srv.delete(dto);
        return ApiResult.ok("entity.crud.delete.success");
    }

    //endregion

}
