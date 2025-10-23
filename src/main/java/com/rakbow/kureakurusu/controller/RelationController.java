package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ApiResult list(@RequestBody RelationListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

    @PostMapping("create")
    public ApiResult create(@RequestBody RelationCreateDTO dto) {
        srv.create(dto);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("update")
    public ApiResult update(@RequestBody RelationUpdateDTO dto) {
        srv.update(dto);
        return ApiResult.ok("entity.crud.update.success");
    }

    @PostMapping("personnel")
    public ApiResult personnel(@RequestBody PersonnelDTO dto) {
        return new ApiResult().load(srv.personnel(dto.getEntityType(), dto.getEntityId()));
    }

    @PostMapping("items")
    public ApiResult relatedItems(@RequestBody RelatedItemQueryDTO dto) {
        return new ApiResult().load(srv.relatedItems(dto));
    }

    @PostMapping("entries")
    public ApiResult relatedEntries(@RequestBody RelatedEntryQueryDTO dto) {
        return new ApiResult().load(srv.relatedEntries(dto));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody CommonDeleteDTO dto) {
        srv.removeByIds(dto.getIds());
        return ApiResult.ok("entity.crud.delete.success");
    }

}
