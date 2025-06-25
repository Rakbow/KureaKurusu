package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.resource.ImageVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.service.ResourceService;
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

    @PostMapping("image/list")
    public ApiResult getEntityImages(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.getEntityImages(new ImageListQueryDTO(dto)));
    }

    @PostMapping("image/default-displayed")
    public ApiResult getEntityImages(@RequestBody EntityQry qry) {
        return new ApiResult().load(srv.getEntityDisplayImages(qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("image/upload")
    public ApiResult addEntityImage(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") int entityId,
            @RequestParam("infos") String infoStr,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("generateThumb") boolean generateThumb
    ) {
        List<ImageCreateDTO> infos = JsonUtil.toJavaList(infoStr, ImageCreateDTO.class);
        List<ImageMiniDTO> images = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            images.add(new ImageMiniDTO(infos.get(i), files.get(i)));
        }
        //check
        if (images.isEmpty()) throw ErrorFactory.fileEmpty();
        //save
        srv.uploadEntityImage(entityType, entityId, images, generateThumb);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("image/update")
    public ApiResult updateEntityImage(@Valid @RequestBody ImageUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        srv.updateEntityImage(converter.convert(dto, Image.class));
        return new ApiResult().ok("entity.crud.update.success");
    }

    @DeleteMapping("image/delete")
    public ApiResult deleteEntityImages(@RequestBody List<ImageVO> images) {
        srv.deleteEntityImage(images);
        return new ApiResult().ok("entity.crud.delete.success");
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
        srv.updateFile(dto);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @PostMapping("file/upload")
    public ApiResult uploadFiles(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") long entityId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("names") List<String> names,
            @RequestParam("remarks") List<String> remarks
    ) {
        srv.uploadFiles(entityType, entityId, files, names, remarks);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @PostMapping("file/create-related")
    public ApiResult createFileRelated(@RequestBody FileCreateDTO dto) {
        srv.createFileRelated(dto.getEntityType(), dto.getEntityId(), dto.getFileIds());
        return new ApiResult().ok("entity.crud.update.success");
    }

    @PostMapping("file/search")
    public ApiResult search(@RequestBody FileSearchParams param) {
        return new ApiResult().load(srv.searchFiles(param));
    }

}
