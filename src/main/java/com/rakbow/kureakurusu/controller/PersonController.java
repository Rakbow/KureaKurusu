package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.EntityQuery;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.data.dto.person.PersonDetailQry;
import com.rakbow.kureakurusu.data.dto.person.PersonUpdateCmd;
import com.rakbow.kureakurusu.data.dto.person.PersonnelManageCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.person.PersonDetailVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.entity.PersonRole;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-14 20:53
 * @Description:
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService service;
    private final GeneralService generalService;
    private final PersonVOMapper voMapper = PersonVOMapper.INSTANCES;
    private final int PERSON_ENTITY = Entity.PERSON.getId();

    //region person

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getPersonDetailData(@RequestBody PersonDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            Person person = service.getPerson(qry.getId());
            if (person == null) {
                res.setErrorMessage(I18nHelper.getMessage("entity.url.error", Entity.PERSON.getNameZh()));
                return res;
            }
            res.data = PersonDetailVO.builder()
                    .item(voMapper.toVO(person))
                    .traffic(generalService.getPageTraffic(PERSON_ENTITY, qry.getId()))
                    .build();
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("search")
    public ApiResult searchPerson(@RequestBody SearchQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.searchPersons(new SimpleSearchParam(qry));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult getPersons(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.getPersons(new QueryParams(json));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("add")
    public ApiResult addPerson(@Valid @RequestBody Person person, BindingResult bindingResult) {
        ApiResult res = new ApiResult();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                res.setErrorMessage(errors);
                return res;
            }
            service.addPerson(person);
            res.message = I18nHelper.getMessage("entity.curd.insert.success", Entity.PERSON.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("update")
    public ApiResult updatePerson(@Valid @RequestBody PersonUpdateCmd cmd, BindingResult bindingResult) {
        ApiResult res = new ApiResult();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                res.setErrorMessage(errors);
                return res;
            }
            service.updatePerson(cmd);
            res.message = I18nHelper.getMessage("entity.curd.update.success", Entity.PERSON.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }
    //endregion

    //region role

    @PostMapping("get-roles")
    public ApiResult getPersonRoles(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.getRoles(new QueryParams(json));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("add-role")
    public ApiResult addPersonRole(@Valid @RequestBody PersonRole role, BindingResult bindingResult) {
        ApiResult res = new ApiResult();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                res.setErrorMessage(errors);
                return res;
            }
            service.addRole(role);
            res.message = I18nHelper.getMessage("entity.curd.insert.success", Entity.ENTRY.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("update-role")
    public ApiResult updateRole(@Valid @RequestBody PersonRole role, BindingResult bindingResult) {
        ApiResult res = new ApiResult();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                res.setErrorMessage(errors);
                return res;
            }
            service.updateRole(role);
            res.message = I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    //endregion

    //region relation

    @PostMapping("get-personnel")
    public ApiResult getPersonnel(@RequestBody EntityQuery qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = service.getPersonnel(qry.getEntityType(), qry.getEntityId());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    @PostMapping("manage-personnel")
    public ApiResult managePersonnel(@RequestBody PersonnelManageCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            service.managePersonnel(cmd);
            res.message = I18nHelper.getMessage("entity.curd.update.success", Entity.ENTRY.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res;
    }

    //endregion

}
