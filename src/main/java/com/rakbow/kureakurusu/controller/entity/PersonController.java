package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.dto.base.SearchQry;
import com.rakbow.kureakurusu.data.dto.person.PersonAddDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonDetailQry;
import com.rakbow.kureakurusu.data.dto.person.PersonUpdateDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonnelManageCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author Rakbow
 * @since 2023-11-14 20:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/person")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonService srv;
    private final PersonVOMapper VOMapper;

    //region person

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getPersonDetailData(@RequestBody PersonDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.loadData(srv.detail(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("search")
    public ApiResult searchPerson(@RequestBody SearchQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.searchPersons(new SimpleSearchParam(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult getPersons(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.getPersons(new QueryParams(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
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
            Person person = VOMapper.build(dto);
            //save
            srv.save(person);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.PERSON.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
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
            srv.updatePerson(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.PERSON.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }
    //endregion

    //region role

    @PostMapping("get-roles")
    public ApiResult getPersonRoles(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.getRoles(new QueryParams(qry));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
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
            srv.addRole(role);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ENTRY.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
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
            srv.updateRole(role);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

    //region relation

    @PostMapping("get-personnel")
    public ApiResult getPersonnel(@RequestBody EntityQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.getPersonnel(qry.getEntityType(), qry.getEntityId());
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("manage-personnel")
    public ApiResult managePersonnel(@RequestBody PersonnelManageCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            srv.managePersonnel(cmd);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

}
