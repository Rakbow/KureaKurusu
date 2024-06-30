package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.service.RelationService;
import com.rakbow.kureakurusu.service.RoleService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
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
        return new ApiResult().load(srv.getRelatedEntity(qry.getRelatedGroup(), qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("get-relations")
    public ApiResult getRelations(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(srv.getRelations(new RelationListParams(qry)));
    }

    @PostMapping("add-relations")
    public ApiResult getPersonnel(@RequestBody RelationCreateDTO dto) {
        srv.addRelations(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success"));
    }

    @PostMapping("update-relation")
    public ApiResult getPersonnel(@RequestBody RelationUpdateDTO dto) {
        srv.updateRelation(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success"));
    }

    @DeleteMapping("delete-relations")
    public ApiResult deletePersonnel(@RequestBody CommonDeleteDTO dto) {
        srv.deleteRelations(dto.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success"));
    }

    @PostMapping("get-roles")
    public ApiResult getRoles(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(roleSrv.getRoles(new RoleListParams(qry)));
    }

    @PostMapping("add-role")
    public ApiResult addRole(@Valid @RequestBody Role role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.save(role);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", EntityType.ROLE.getLabel()));
    }

    @PostMapping("update-role")
    public ApiResult updateRole(@Valid @RequestBody Role role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.updateById(role);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", EntityType.ROLE.getLabel()));
    }
}
