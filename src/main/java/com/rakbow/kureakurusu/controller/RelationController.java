package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.dto.DeleteCmd;
import com.rakbow.kureakurusu.data.dto.RelationDTO;
import com.rakbow.kureakurusu.data.dto.RelationManageCmd;
import com.rakbow.kureakurusu.data.dto.RelationQry;
import com.rakbow.kureakurusu.data.entity.EntityRelation;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.service.RelationService;
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

    @PostMapping("get-relations")
    public ApiResult getRelations(@RequestBody RelationQry qry) {
        return new ApiResult().load(srv.getRelations(qry));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody RelationDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        EntityRelation relation = new EntityRelation(dto);
        //save
        srv.save(relation);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", I18nHelper.getMessage("enum.entity.relation")));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody RelationDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateRelation(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", I18nHelper.getMessage("enum.entity.relation")));
    }

    @DeleteMapping("delete")
    public ApiResult deleteRelations(@RequestBody DeleteCmd cmd) {
        srv.deleteRelations(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success", I18nHelper.getMessage("enum.entity.relation")));
    }

    @PostMapping("manage-relation")
    public ApiResult manageRelation(@RequestBody RelationManageCmd cmd) {
        srv.manageRelation(cmd);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", I18nHelper.getMessage("enum.entity.relation")));
    }

}
