package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.service.PersonRoleService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import io.github.linpeilie.Converter;
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
    private final Converter converter;
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
    public ApiResult list(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(srv.getPersons(new PersonListParams(qry)));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody PersonAddDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Person person = converter.convert(dto, Person.class);
        //save
        srv.save(person);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", EntityType.PERSON.getLabel()));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody PersonUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateById(new Person(dto));
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", EntityType.PERSON.getLabel()));
    }
    //endregion

    //region role

    @PostMapping("get-roles")
    public ApiResult getPersonRoles(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(roleSrv.getRoles(new PersonRoleListParams(qry)));
    }

    @PostMapping("add-role")
    public ApiResult addPersonRole(@Valid @RequestBody PersonRole role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.save(role);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", EntityType.ROLE.getLabel()));
    }

    @PostMapping("update-role")
    public ApiResult updateRole(@Valid @RequestBody PersonRole role, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        roleSrv.updateById(role);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", EntityType.ROLE.getLabel()));
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
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success"));
    }

    //endregion

}
