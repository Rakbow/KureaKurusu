package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.dto.common.DeleteCmd;
import com.rakbow.kureakurusu.data.dto.relation.RelationDTO;
import com.rakbow.kureakurusu.data.dto.relation.RelationQry;
import com.rakbow.kureakurusu.data.entity.EntityRelation;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.service.RelationService;
import com.rakbow.kureakurusu.util.I18nHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2024/2/28 15:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/relation")
public class RelationController {

    private static final Logger log = LoggerFactory.getLogger(RelationController.class);
    private final RelationService srv;

    @PostMapping("get-relations")
    public ApiResult getRelations(@RequestBody RelationQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.loadData(srv.getRelations(qry.getEntityType(), qry.getEntityId()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("add")
    public ApiResult addRelation(@Valid @RequestBody RelationDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            EntityRelation relation = new EntityRelation(dto);
            //save
            srv.save(relation);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", I18nHelper.getMessage("enum.entity.relation")));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("update")
    public ApiResult updateRelation(@Valid @RequestBody RelationDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            srv.updateRelation(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", I18nHelper.getMessage("enum.entity.relation")));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @DeleteMapping("delete")
    public ApiResult deleteRelations(@RequestBody DeleteCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            srv.deleteRelations(cmd.getIds());
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", I18nHelper.getMessage("enum.entity.relation")));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
