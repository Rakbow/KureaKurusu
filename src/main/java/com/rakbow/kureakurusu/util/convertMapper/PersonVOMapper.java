package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.annotation.ToUpdate;
import com.rakbow.kureakurusu.annotation.ToVO;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.data.dto.person.PersonAddDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonUpdateDTO;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.util.common.DateHelper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-06 5:55
 */
@Mapper(componentModel = "spring")
public interface PersonVOMapper extends MetaVOMapper {

    PersonVOMapper INSTANCES = Mappers.getMapper(PersonVOMapper.class);

    @Mapping(source = "gender", target = "gender", qualifiedByName = "getGender")
    @Named("build")
    Person build(PersonAddDTO dto);

//    @Mapping(source = "gender", target = "gender", qualifiedByName = "getGender")
//    @Mapping(target = "cover",  constant = "null")
//    @Mapping(target = "info",  constant = "null")
//    @Mapping(target = "links",  constant = "null")
//    @ToUpdate
//    @Named("build")
//    Person build(PersonUpdateDTO dto);

    default Person build(PersonUpdateDTO dto) {
        if (dto == null) return null;
        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setNameZh(dto.getNameZh());
        person.setNameEn(dto.getNameEn());
        person.setAliases(dto.getAliases());
        person.setGender(Gender.get(dto.getGender()));
        person.setBirthDate(dto.getBirthDate());
        person.setRemark(dto.getRemark());
        person.setEditedTime(DateHelper.now());

        person.setCover(null);
        person.setLinks(null);
        person.setInfo(null);
        person.setImages(null);
        person.setDetail(null);
        person.setStatus(null);
        person.setAddedTime(null);
        return person;
    }

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @ToVO
    PersonVO toVO(Person person);

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @Named("toMiniVO")
    PersonMiniVO toMiniVO(Person person);

    @IterableMapping(qualifiedByName = "toMiniVO")
    List<PersonMiniVO> toMiniVO(List<Person> persons);

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @Mapping(source = "addedTime", target = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(source = "editedTime", target = "editedTime", qualifiedByName = "getVOTime")
    @Named("toBetaVO")
    PersonVOBeta toBetaVO(Person person);

    @IterableMapping(qualifiedByName = "toBetaVO")
    List<PersonVOBeta> toBetaVO(List<Person> persons);

}
