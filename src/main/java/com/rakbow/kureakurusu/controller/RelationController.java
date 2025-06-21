package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.service.RelationService;
import com.rakbow.kureakurusu.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
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
    private final RoleService roleSrv;

    @PostMapping("get-related-entity")
    public ApiResult getRelatedEntity(@RequestBody RelationQry qry) {
        return new ApiResult().load(srv.getSimpleRelatedEntity(qry.getDirection(), qry.getRelatedGroup(), qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("get-related-entities")
    public ApiResult getRelatedEntities(@RequestBody RelationQry qry) {
        return new ApiResult().load(srv.getRelatedEntities(qry));
    }

    @PostMapping("list")
    public ApiResult getRelations(@RequestBody ListQuery qry) {
        return new ApiResult().load(srv.getRelations(new RelationListQueryDTO(qry)));
    }

    @PostMapping("create")
    public ApiResult getPersonnel(@RequestBody RelationCreateDTO dto) {
        srv.addRelatedEntries(dto);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("update")
    public ApiResult getPersonnel(@RequestBody RelationUpdateDTO dto) {
        srv.updateRelation(dto);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    public ApiResult deletePersonnel(@RequestBody CommonDeleteDTO dto) {
        srv.removeByIds(dto.getIds());
        return new ApiResult().ok("entity.crud.delete.success");
    }

    @PostMapping("get-roles")
    public ApiResult getRoles(@RequestBody ListQuery qry) {
        return new ApiResult().load(roleSrv.getRoles(new RoleListQueryDTO(qry)));
    }

    @PostMapping("add-role")
    public ApiResult addRole(@Valid @RequestBody Role role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.save(role);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("update-role")
    public ApiResult updateRole(@Valid @RequestBody Role role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.updateById(role);
        return new ApiResult().ok("entity.crud.update.success");
    }
}
