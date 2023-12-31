package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.dto.person.PersonAddDTO;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.data.entity.Person;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-06 5:55
 */
@Mapper(componentModel = "spring")
public interface PersonVOMapper extends CommonVOMapper {

    PersonVOMapper INSTANCES = Mappers.getMapper(PersonVOMapper.class);

    @Mapping(source = "gender", target = "gender", qualifiedByName = "getGender")
    @Named("build")
    Person build(PersonAddDTO dto);

    @Mapping(source = "gender.labelKey", target = "gender.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "gender.value", target = "gender.value")
    @Mapping(source = "status", target = "status", qualifiedByName = "getStatus")
    @Mapping(source = "addedTime", target = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(source = "editedTime", target = "editedTime", qualifiedByName = "getVOTime")
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
    @Mapping(source = "addedTime", target = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(source = "editedTime", target = "editedTime", qualifiedByName = "getVOTime")
    @Named("toBetaVO")
    PersonVOBeta toBetaVO(Person person);

    @IterableMapping(qualifiedByName = "toBetaVO")
    List<PersonVOBeta> toBetaVO(List<Person> persons);

}
