package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.dto.EntityMinDTO;
import com.rakbow.kureakurusu.data.dto.EntrySearchParams;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.service.EntryService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/2 21:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/entry")
public class EntryController {

    private final EntryService srv;

    @UniqueVisitor
    @PostMapping("detail/{type}/{id}")
    public ApiResult detail(@PathVariable("type") int type, @PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(type, id));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody EntryUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        return new ApiResult().ok(srv.update(dto));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.list(dto));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody EntrySearchParams param) {
        return new ApiResult().load(srv.search(param));
    }

    @PostMapping("upload-image")
    public ApiResult uploadImage(
            @RequestPart("entityType") int entityType,
            @RequestPart("entityId") int entityId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("imageType") int imageType
    ) {
        ApiResult res = new ApiResult();
        //check
        ImageMiniDTO image = new ImageMiniDTO();
        image.setFile(file);
        image.setType(imageType);
        if (file.isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        res.load(srv.uploadImage(entityType, entityId, image));
        return res.ok(I18nHelper.getMessage("image.update.success"));
    }

    @PostMapping("mini")
    public ApiResult mini(@RequestBody List<EntityMinDTO> entries) {
        return new ApiResult().load(srv.getMiniVO(entries));
    }

}
