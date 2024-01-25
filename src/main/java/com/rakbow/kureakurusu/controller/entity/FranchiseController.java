package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseAddDTO;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseDetailQry;
import com.rakbow.kureakurusu.data.dto.franchise.FranchiseUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseDetailVO;
import com.rakbow.kureakurusu.service.FranchiseService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.FranchiseVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    private final FranchiseService service;
    private final EntityUtil entityUtil;
    private final FranchiseVOMapper voMapper = FranchiseVOMapper.INSTANCES;
    private final int ENTITY_VALUE = Entity.FRANCHISE.getValue();

    //endregion

    //region ------crud------

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getFranchiseDetailData(@RequestBody FranchiseDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            Franchise item = service.getById(qry.getId());
            if (item == null)
                return res.fail(I18nHelper.getMessage("entity.url.error", Entity.FRANCHISE.getName()));

            res.data = FranchiseDetailVO.builder()
                    .item(voMapper.toVO(item))
                    .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                    .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                    .itemImageInfo(CommonImageUtil.segmentImages(item.getImages(), 200, Entity.FRANCHISE, false))
                    .build();
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult getFranchises(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.list(new QueryParams(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
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
            service.save(item);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.FRANCHISE.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
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
            service.update(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.FRANCHISE.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

}
