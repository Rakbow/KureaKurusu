package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.annotation.ToVO;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.ProductAddDTO;
import com.rakbow.kureakurusu.data.emun.ProductCategory;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.product.ProductMiniVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.util.common.DataFinder;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Product VO转换接口
 */
@Mapper(componentModel = "spring")
public interface ProductVOMapper extends MetaVOMapper {

    ProductVOMapper INSTANCES = Mappers.getMapper(ProductVOMapper.class);

    @Mapping(source = "category", target = "category", qualifiedByName = "getCategory")
    @Named("build")
    Product build(ProductAddDTO dto);

    @Mapping(source = "category.labelKey", target = "category.label", qualifiedByName = "getEnumLabel")
    @Mapping(source = "category.value", target = "category.value")
    @Mapping(source = "franchise", target = "franchise", qualifiedByName = "getFranchise")
    @ToVO
    @Named("toVO")
    ProductVO toVO(Product product);

    @Mapping(target = "cover", source = "images", qualifiedByName = "getThumbCover")
    @Mapping(source = "category.labelKey", target = "category.label", qualifiedByName = "getEnumLabel")
    @Mapping(source = "category.value", target = "category.value")
    @Named("toMiniVO")
    ProductMiniVO toMiniVO(Product product);

    @IterableMapping(qualifiedByName = "toMiniVO")
    List<ProductMiniVO> toMiniVO(List<Product> products);

    @Mapping(source = "category", target = "category", qualifiedByName = "getCategoryAttribute")
    @Mapping(source = "franchise", target = "franchise", qualifiedByName = "getFranchise")
    @ToVO
    @Named("toVOAlpha")
    ProductVOAlpha toVOAlpha(Product product);

    @IterableMapping(qualifiedByName = "toVOAlpha")
    List<ProductVOAlpha> toVOAlpha(List<Product> products);

    @Named("getCategory")
    default ProductCategory getCategory(int value) {
        return ProductCategory.get(value);
    }

    @Named("getFranchise")
    default Attribute<Long> getFranchise(long value) {
        return DataFinder.findAttributeByValue(value, Objects.requireNonNull(MetaData.getOptions()).franchiseSet);
    }

    @Named("getCategoryAttribute")
    default Attribute<Integer> getCategoryAttribute(int value) {
        return DataFinder.findAttributeByValue(value, Objects.requireNonNull(MetaData.getOptions()).productCategorySet);
    }

}
