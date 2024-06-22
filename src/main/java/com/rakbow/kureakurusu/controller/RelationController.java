package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.CommonDeleteDTO;
import com.rakbow.kureakurusu.data.dto.RelationCreateDTO;
import com.rakbow.kureakurusu.data.dto.RelationQry;
import com.rakbow.kureakurusu.data.dto.RelationUpdateDTO;
import com.rakbow.kureakurusu.service.RelationService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
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

    @PostMapping("get-relations")
    public ApiResult getRelations(@RequestBody RelationQry qry) {
        return new ApiResult().load(srv.getRelations(qry.getRoleGroup(), qry.getEntityType(), qry.getEntityId()));
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

}
