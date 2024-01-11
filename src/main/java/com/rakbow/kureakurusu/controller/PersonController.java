package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.base.SearchQry;
import com.rakbow.kureakurusu.data.dto.person.PersonAddDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonDetailQry;
import com.rakbow.kureakurusu.data.dto.person.PersonUpdateDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonnelManageCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.person.PersonDetailVO;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
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
 * @since 2023-11-14 20:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/person")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonService service;
    private final GeneralService generalService;
    private final PersonVOMapper voMapper = PersonVOMapper.INSTANCES;
    private final EntityUtil entityUtil;
    private final int ENTITY_VALUE = Entity.PERSON.getValue();

    //region person

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getPersonDetailData(@RequestBody PersonDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            Person person = service.getById(qry.getId());
            if (person == null)
                return res.fail(I18nHelper.getMessage("entity.url.error", Entity.PERSON.getName()));

            res.data = PersonDetailVO.builder()
                    .item(voMapper.toVO(person))
                    .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                    .build();
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("search")
    public ApiResult searchPerson(@RequestBody SearchQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.searchPersons(new SimpleSearchParam(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult getPersons(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.getPersons(new QueryParams(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("add")
    public ApiResult addPerson(@Valid @RequestBody PersonAddDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            Person person = voMapper.build(dto);
            //save
            service.save(person);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.PERSON.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("update")
    public ApiResult updatePerson(@Valid @RequestBody PersonUpdateDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            service.updatePerson(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.PERSON.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }
    //endregion

    //region role

    @PostMapping("get-roles")
    public ApiResult getPersonRoles(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.getRoles(new QueryParams(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("add-role")
    public ApiResult addPersonRole(@Valid @RequestBody PersonRole role, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            service.addRole(role);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ENTRY.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("update-role")
    public ApiResult updateRole(@Valid @RequestBody PersonRole role, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            service.updateRole(role);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

    //region relation

    @PostMapping("get-personnel")
    public ApiResult getPersonnel(@RequestBody EntityQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.getPersonnel(qry.getEntityType(), qry.getEntityId());
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("manage-personnel")
    public ApiResult managePersonnel(@RequestBody PersonnelManageCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            service.managePersonnel(cmd);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

}
