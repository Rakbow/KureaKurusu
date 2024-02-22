package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseAddDTO;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseDeleteCmd;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseDetailQry;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.service.FranchiseService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.FranchiseVOMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @author Rakbow
 * @since 2023-01-14 17:07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/franchise")
public class FranchiseController {

    private static final Logger log = LoggerFactory.getLogger(FranchiseController.class);

    //region ------inject------

    private final FranchiseService srv;
    private final FranchiseVOMapper voMapper;

    //endregion

    //region ------crud------

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getFranchiseDetail(@RequestBody FranchiseDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.detail(qry);
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult getFranchises(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.list(new QueryParams(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("add")
    public ApiResult addFranchise(@Valid @RequestBody FranchiseAddDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            Franchise item = voMapper.build(dto);
            //save
            srv.save(item);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.FRANCHISE.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("update")
    public ApiResult updateFranchise(@Valid @RequestBody FranchiseUpdateDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            srv.update(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.FRANCHISE.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @DeleteMapping("delete")
    public ApiResult deleteFranchise(@RequestBody FranchiseDeleteCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            srv.delete(cmd.getIds());
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.FRANCHISE.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

}
