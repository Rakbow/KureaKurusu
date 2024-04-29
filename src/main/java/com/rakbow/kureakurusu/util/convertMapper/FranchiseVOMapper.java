package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.annotation.ToVO;
import com.rakbow.kureakurusu.data.dto.FranchiseAddDTO;
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
@Mapper(componentModel = "spring", uses = MetaVOMapper.class)
public interface FranchiseVOMapper {

    FranchiseVOMapper INSTANCES = Mappers.getMapper(FranchiseVOMapper.class);

    @ToVO
    @Named("toVO")
    FranchiseVO toVO(Franchise franchise);

    @IterableMapping(qualifiedByName = "toVO")
    List<FranchiseVO> toVO(List<Franchise> franchises);

    @Named("build")
    Franchise build(FranchiseAddDTO dto);

}
