package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ProductListParams;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.data.vo.product.ProductListVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVO;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.VisitUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rakbow
 * @since 2022-08-20 2:02
 */
@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    //region ------inject------
    private final ProductMapper mapper;
    private final RelationMapper relationMapper;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final Converter converter;

    private final ResourceService resourceSrv;
    //endregion

    //region const
    private final EntityType ENTITY_VALUE = EntityType.PRODUCT;

    //endregion

    @Transactional
    public SearchResult<ProductListVO> list(ProductListParams param) {
        QueryWrapper<Product> wrapper = new QueryWrapper<Product>()
                .like("name", param.getName())
                .in(CollectionUtils.isNotEmpty(param.getType()), "type", param.getType())
                .orderBy(param.isSort(), param.asc(), param.sortField);
        IPage<Product> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<ProductListVO> items = converter.convert(pages.getRecords(), ProductListVO.class);
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @SneakyThrows
    @Transactional
    public ProductDetailVO getDetail(long id) {
        Product product = getById(id);
        if(product == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", EntityType.PRODUCT.getLabel()));
        return ProductDetailVO.builder()
                .item(converter.convert(product, ProductVO.class))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE.getValue()))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE.getValue(), id))
                .cover(resourceSrv.getEntityCover(ENTITY_VALUE, id))
                .images(resourceSrv.getDefaultImages(ENTITY_VALUE.getValue(), id))
                .imageCount(resourceSrv.getDefaultImagesCount(ENTITY_VALUE.getValue(), id))
                .build();
    }

    @Transactional
    public void deleteProducts(List<Long> ids) {
        //get original data
        List<Product> items = mapper.selectBatchIds(ids);
        for (Product item : items) {
            //delete all image
//            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.del(ENTITY_VALUE.getValue(), item.getId());
        }
        //delete
        mapper.delete(new LambdaQueryWrapper<Product>().in(Product::getId, ids));
        //delete person relation
        relationMapper.delete(new LambdaQueryWrapper<Relation>().eq(Relation::getEntityType, ENTITY_VALUE).in(Relation::getEntityId, ids));
    }

    //endregion
}
