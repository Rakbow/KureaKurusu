package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.context.MessageSource;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 5:55
 * @Description:
 */
@Mapper(componentModel = "spring")
public interface PersonVOMapper extends CommonVOMapper {

    PersonVOMapper INSTANCES = Mappers.getMapper(PersonVOMapper.class);

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @Mapping(source = "aliases", target = "aliases", qualifiedByName = "getStrList")
    @Named("toVO")
    PersonVO toVO(Person person);

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @Named("toMiniVO")
    PersonMiniVO toMiniVO(Person person);

    @IterableMapping(qualifiedByName = "toMiniVO")
    List<PersonMiniVO> toMiniVO(List<Person> persons);

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @Mapping(source = "aliases", target = "aliases", qualifiedByName = "getStrList")
    @Mapping(source = "addedTime", target = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(source = "editedTime", target = "editedTime", qualifiedByName = "getVOTime")
    @Named("toBetaVO")
    PersonVOBeta toBetaVO(Person person);

    @IterableMapping(qualifiedByName = "toBetaVO")
    List<PersonVOBeta> toBetaVO(List<Person> persons);

}
