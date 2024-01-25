package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.data.dto.person.PersonAddDTO;
import com.rakbow.kureakurusu.data.dto.product.ProductAddDTO;
import com.rakbow.kureakurusu.data.dto.product.ProductUpdateDTO;
import com.rakbow.kureakurusu.data.emun.entity.album.AlbumFormat;
import com.rakbow.kureakurusu.data.emun.entity.product.ProductCategory;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.product.ProductVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.data.vo.product.ProductVOBeta;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.entry.EntryUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Product VO转换接口
 */
@Mapper(componentModel = "spring")
public interface ProductVOMapper extends CommonVOMapper {

    ProductVOMapper INSTANCES = Mappers.getMapper(ProductVOMapper.class);

    @Mapping(source = "category", target = "category", qualifiedByName = "getCategory")
    @Named("build")
    Product build(ProductAddDTO dto);

    @Mapping(source = "category.labelKey", target = "category.label", qualifiedByName = "getEnumLabel")
    @Mapping(source = "category.value", target = "category.value")
    @Mapping(target = "franchise", ignore = true)
    @ToVO
    @Named("toVO")
    ProductVO toVO(Product product);

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
