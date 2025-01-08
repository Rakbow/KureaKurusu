package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.dto.PersonAddDTO;
import com.rakbow.kureakurusu.data.dto.PersonListParams;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Person;
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
    private final Converter converter;
    //endregion

    //region person

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

}
