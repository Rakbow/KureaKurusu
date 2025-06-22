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

    @PostMapping("get-related-entity")
    public ApiResult getRelatedEntity(@RequestBody RelationQry qry) {
        return new ApiResult().load(srv.getSimpleRelatedEntity(qry.getDirection(), qry.getRelatedGroup(), qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("get-related-entities")
    public ApiResult getRelatedEntities(@RequestBody RelationQry qry) {
        return new ApiResult().load(srv.getRelatedEntities(qry));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQuery qry) {
        return new ApiResult().load(srv.getRelations(new RelationListQueryDTO(qry)));
    }

    @PostMapping("create")
    public ApiResult create(@RequestBody RelationCreateDTO dto) {
        srv.create(dto);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("update")
    public ApiResult update(@RequestBody RelationUpdateDTO dto) {
        srv.update(dto);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody CommonDeleteDTO dto) {
        srv.removeByIds(dto.getIds());
        return new ApiResult().ok("entity.crud.delete.success");
    }

}
