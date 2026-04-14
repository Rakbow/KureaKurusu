package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

/**
 * @author Rakbow
 * @since 2024/2/28 15:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/relation")
public class RelationController {

    private final RelationService srv;

    @PostMapping("list")
    public R list(@RequestBody RelationListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    @PostMapping("create")
    @Permission(RELATION_CREATE)
    public R create(@RequestBody RelationCreateDTO dto) {
        srv.create(dto);
        return R.ok("entity.crud.create.success");
    }

    @PostMapping("update")
    @Permission(RELATION_UPDATE)
    public R update(@RequestBody RelationUpdateDTO dto) {
        srv.update(dto);
        return R.ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    @Permission(RELATION_DELETE)
    public R delete(@RequestBody CommonDeleteDTO dto) {
        srv.removeByIds(dto.ids());
        srv.refreshPersonnel(dto.entityType(), dto.entityId());
        return R.ok("entity.crud.delete.success");
    }

    @PostMapping("personnel")
    public R personnel(@RequestBody EntityDTO dto) {
        return R.ok(srv.personnel(dto.entityType(), dto.entityId()));
    }

    @PostMapping("items")
    public R relatedItems(@RequestBody RelatedItemQueryDTO dto) {
        return R.ok(srv.relatedItems(dto));
    }

    @PostMapping("entries")
    public R relatedEntries(@RequestBody RelatedEntryQueryDTO dto) {
        return R.ok(srv.relatedEntries(dto));
    }

}
