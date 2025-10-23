package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/10 1:19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/file")
public class FileController {

    private final FileService srv;

    @PostMapping("upload")
    public ApiResult upload(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") long entityId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("names") List<String> names
    ) {
        srv.upload(entityType, entityId, files, names);
        return ApiResult.ok("entity.crud.update.success");
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody FileUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        srv.update(dto);
        return ApiResult.ok("entity.crud.update.success");
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody FileListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody FileSearchParams param) {
        return new ApiResult().load(srv.search(param));
    }

    @PostMapping("related")
    public ApiResult related(@RequestBody EntityQryDTO dto) {
        return new ApiResult().load(srv.related(dto));
    }

    @PostMapping("related-create")
    public ApiResult createRelated(@RequestBody FileCreateDTO dto) {
        srv.createRelated(dto.getEntityType(), dto.getEntityId(), dto.getFileIds());
        return ApiResult.ok("entity.crud.update.success");
    }

    @DeleteMapping("related-delete")
    public ApiResult deleteRelated(@RequestBody FileRelatedDeleteDTO dto) {
        srv.deleteRelated(dto);
        return ApiResult.ok("entity.crud.delete.success");
    }

}
