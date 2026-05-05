package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.CommonDeleteDTO;
import com.rakbow.kureakurusu.data.dto.EntityDTO;
import com.rakbow.kureakurusu.data.dto.ResourceDTO;
import com.rakbow.kureakurusu.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

/**
 * @author Rakbow
 * @since 2026/5/1 18:49
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("db/resource")
public class ResourceController {

    private final ResourceService srv;

    @SneakyThrows
    @PostMapping("local-path")
    @Permission(FILE_LOCAL_PATH)
    public R localPath(@RequestBody EntityDTO dto) {
        srv.getLocalPath(dto.entityType(), dto.entityId());
        return R.ok();
    }

    @SneakyThrows
    @PostMapping("update-resource-flag")
    @Permission(FILE_LOCAL_FLAG_UPDATE)
    public R updateResourceFlag(@RequestBody ResourceDTO.ResourceInfoCommonDTO dto) {
        srv.updateResourceFlag(dto);
        return R.ok("entity.crud.update.success");
    }

    @PostMapping("create")
    @Permission(INDEX_CREATE)
    public R create(@RequestBody ResourceDTO.ResourceInfoCommonDTO dto) {
        srv.create(dto);
        return R.ok("entity.crud.create.success");
    }

    @SneakyThrows
    @PostMapping("update")
    @Permission(RESOURCE_UPDATE)
    public R update(@RequestBody ResourceDTO.ResourceInfoCommonDTO dto) {
        srv.update(dto);
        return R.ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    @Permission(RESOURCE_DELETE)
    public R delete(@RequestBody CommonDeleteDTO dto) {
        srv.delete(dto.ids());
        return R.ok("entity.crud.delete.success");
    }

    @PostMapping("list")
    public R list(@RequestBody ResourceDTO.ResourceInfoListQueryDTO dto) {
        return R.ok(srv.list(dto.getEntityType(), dto.getEntityId()));
    }

}
