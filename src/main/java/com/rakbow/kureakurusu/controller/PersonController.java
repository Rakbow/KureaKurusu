package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntityQry;
import com.rakbow.kureakurusu.data.dto.ListQry;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.service.PersonRoleService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.PersonVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2023-11-14 20:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/person")
public class PersonController {

    //region inject
    private final PersonService srv;
    private final PersonRoleService roleSrv;
    private final PersonVOMapper VOMapper;
    //endregion

    //region person

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody PersonDetailQry qry) {
        return new ApiResult().load(srv.detail(qry));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        return new ApiResult().load(srv.searchPersons(new SimpleSearchParam(qry)));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQry qry) {
        return new ApiResult().load(srv.getPersons(new PersonListParams(qry)));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody PersonAddDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Person person = VOMapper.build(dto);
        //save
        srv.save(person);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.PERSON.getName()));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody PersonUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Person person = VOMapper.build(dto);
        //save
        srv.updateById(person);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.PERSON.getName()));
    }
    //endregion

    //region role

    @PostMapping("get-roles")
    public ApiResult getPersonRoles(@RequestBody ListQry qry) {
        return new ApiResult().load(roleSrv.getRoles(new PersonRoleListParams(qry)));
    }

    @PostMapping("add-role")
    public ApiResult addPersonRole(@Valid @RequestBody PersonRole role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.save(role);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ROLE.getName()));
    }

    @PostMapping("update-role")
    public ApiResult updateRole(@Valid @RequestBody PersonRole role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.updateById(role);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ROLE.getName()));
    }

    //endregion

    //region relation

    @PostMapping("get-personnel")
    public ApiResult getPersonnel(@RequestBody EntityQry qry) {
        return new ApiResult().load(srv.getPersonnel(qry.getEntityType(), qry.getEntityId()));
    }

    @PostMapping("manage-personnel")
    public ApiResult managePersonnel(@RequestBody PersonnelManageCmd cmd) {
        srv.managePersonnel(cmd);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getName()));
    }

    //endregion

}
