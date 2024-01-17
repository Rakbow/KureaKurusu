package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.product.ProductDetailQry;
import com.rakbow.kureakurusu.data.dto.product.ProductUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.ProductVOMapper;
import com.rakbow.kureakurusu.util.entity.ProductUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2022-08-20 2:02
 */
@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    //region ------inject------
    private final ProductMapper mapper;
    private final PersonRelationMapper relationMapper;
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final ProductVOMapper voMapper = ProductVOMapper.INSTANCES;
    private final int ENTITY_VALUE = Entity.PRODUCT.getValue();
    //endregion

    //region ------basic crud------

    @SneakyThrows
    public ProductDetailVO getDetail(ProductDetailQry qry) {
        Product product = getById(qry.getId());
        if(product == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.PRODUCT.getName()));
        return ProductDetailVO.builder()
                .product(voMapper.toVO(product))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .pageInfo(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .itemImageInfo(CommonImageUtil.segmentImages(product.getImages(), 100, Entity.PRODUCT, true))
                .build();
    }

    public void updateProduct(ProductUpdateDTO dto) {
        mapper.update(new LambdaUpdateWrapper<Product>()
                .eq(Product::getId, dto.getId())
                .set(Product::getName, dto.getName())
                .set(Product::getNameZh, dto.getNameEn())
                .set(Product::getNameZh, dto.getNameEn())
                .set(Product::getReleaseDate, dto.getReleaseDate())
                .set(Product::getFranchise, dto.getFranchise())
                .set(Product::getCategory, dto.getCategory().getValue())
                .set(Product::getRemark, dto.getRemark())
                .set(Product::getEditedTime, DateHelper.now())
        );
    }

    public void deleteProducts(List<Long> ids) {
        //get original data
        List<Product> products = mapper.selectBatchIds(ids);
        for (Product product : products) {
            //delete all image
            qiniuFileUtil.deleteAllImage(product.getImages());
            //delete product
            mapper.deleteById(product.getId());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, product.getId());
            //delete person relation
            relationMapper.delete(new LambdaQueryWrapper<PersonRelation>().eq(PersonRelation::getEntityId, product.getId()));
        }
    }

    //endregion

}
