package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.ADMIN;

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
    @Permission(ADMIN)
    public R upload(
            @RequestParam("entityType") int entityType,
            @RequestParam("entityId") long entityId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("names") List<String> names
    ) {
        srv.upload(entityType, entityId, files, names);
        return R.ok("entity.crud.update.success");
    }

    @PostMapping("update")
    @Permission(ADMIN)
    public R update(@Valid @RequestBody FileUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new R().fail(errors);
        //update
        srv.update(dto);
        return R.ok("entity.crud.update.success");
    }

    @PostMapping("list")
    @Permission(ADMIN)
    public R list(@RequestBody FileListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    @PostMapping("search")
    @Permission(ADMIN)
    public R search(@RequestBody FileSearchParams param) {
        return R.ok(srv.search(param));
    }

    @PostMapping("related")
    @Permission(ADMIN)
    public R related(@RequestBody EntityDTO dto) {
        return R.ok(srv.related(dto));
    }

    @PostMapping("related-create")
    @Permission(ADMIN)
    public R createRelated(@RequestBody FileCreateDTO dto) {
        srv.createRelated(dto.entityType(), dto.entityId(), dto.fileIds());
        return R.ok("entity.crud.update.success");
    }

    @DeleteMapping("related-delete")
    @Permission(ADMIN)
    public R deleteRelated(@RequestBody CommonDeleteDTO dto) {
        srv.deleteRelated(dto);
        return R.ok("entity.crud.delete.success");
    }

}
