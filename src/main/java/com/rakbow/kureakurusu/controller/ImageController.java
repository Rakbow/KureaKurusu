package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
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

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

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
    public R list(@RequestBody ImageDTO.ImageListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    @PostMapping("preview")
    public R preview(@RequestBody ImageDTO.ImagePreviewDTO dto) {
        return R.ok(srv.preview(dto));
    }

    @PostMapping("upload")
    @Permission(IMAGE_UPLOAD)
    public R upload(
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
        return R.ok("entity.crud.create.success");
    }

    @PostMapping("update")
    @Permission(IMAGE_UPDATE)
    public R update(@Valid @RequestBody ImageDTO.ImageUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new R().fail(errors);
        //update
        srv.update(dto);
        return R.ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    @Permission(IMAGE_DELETE)
    public R delete(@RequestBody ImageDTO.ImageDeleteDTO dto) {
        srv.delete(dto);
        return R.ok("entity.crud.delete.success");
    }

    //endregion

}
