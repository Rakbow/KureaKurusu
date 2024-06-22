package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.service.RoleService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import io.github.linpeilie.Converter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final RoleService roleSrv;
    private final Converter converter;
    //endregion

    //region person

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody PersonDetailQry qry) {
        return new ApiResult().load(srv.detail(qry));
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
        return new ApiResult().load(roleSrv.getRoles(new RoleListParams(qry)));
    }

    @PostMapping("add-role")
    public ApiResult addPersonRole(@Valid @RequestBody Role role, BindingResult errors) {
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

    //endregion

}
