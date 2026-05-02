package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.EntityDTO;
import com.rakbow.kureakurusu.data.dto.EntityResourceInfoUpdateDTO;
import com.rakbow.kureakurusu.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.FILE_LOCAL_FLAG_UPDATE;
import static com.rakbow.kureakurusu.data.constant.PermissionConstant.FILE_LOCAL_PATH;

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
        srv.getLocalPath(dto.entityType(), dto.entitySubType(), dto.entityId());
        return R.ok();
    }

    @SneakyThrows
    @PostMapping("update-resource-flag")
    @Permission(FILE_LOCAL_FLAG_UPDATE)
    public R updateResourceFlag(@RequestBody EntityResourceInfoUpdateDTO dto) {
        srv.updateResourceFlag(dto);
        return R.ok();
    }
}
