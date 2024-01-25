package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.dto.franchise.FranchiseAddDTO;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Franchise VO转换接口
 */
@Mapper(componentModel = "spring")
public interface FranchiseVOMapper extends CommonVOMapper {

    FranchiseVOMapper INSTANCES = Mappers.getMapper(FranchiseVOMapper.class);

    @ToVO
    @Named("toVO")
    FranchiseVO toVO(Franchise franchise);

    @IterableMapping(qualifiedByName = "toVO")
    List<FranchiseVO> toVO(List<Franchise> franchises);

    @Named("build")
    Franchise build(FranchiseAddDTO dto);

}
