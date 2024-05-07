package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.ProductDetailQry;
import com.rakbow.kureakurusu.data.dto.ProductListParams;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.ProductCategory;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVOAlpha;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.data.vo.product.ProductMiniVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.VisitUtil;
import com.rakbow.kureakurusu.toolkit.convert.EpisodeVOMapper;
import com.rakbow.kureakurusu.toolkit.convert.ProductVOMapper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final EpisodeMapper epMapper;
    private final PersonRelationMapper relationMapper;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final ProductVOMapper VOMapper;
    private final EpisodeVOMapper epVOMapper;
    //endregion

    //region const
    private final int ENTITY_VALUE = Entity.PRODUCT.getValue();
    private static final ProductCategory[] VIDEO_PRODUCT = {
            ProductCategory.ANIME_TV,
            ProductCategory.ANIME_MOVIE,
            ProductCategory.LIVE_ACTION_MOVIE,
            ProductCategory.TV_SERIES, ProductCategory.OVA_OAD
    };

    //endregion

    //region ------basic crud------

    @Transactional
    public List<ProductMiniVO> getRelatedProducts(long franchiseId) {
        List<Product> products = mapper.selectList(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getFranchise, franchiseId)
                        .eq(Product::getStatus, 1)
        );
        return VOMapper.toMiniVO(products);
    }

    @Transactional
    public SearchResult<ProductMiniVO> searchProducts(SearchQry qry) {
        SimpleSearchParam param = new SimpleSearchParam(qry);
        if(param.keywordEmpty()) new SearchResult<>();
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .or().like(Product::getName, param.getKeyword())
                .or().like(Product::getNameZh, param.getKeyword())
                .or().like(Product::getNameEn, param.getKeyword())
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getId);
        IPage<Product> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<ProductMiniVO> items = VOMapper.toMiniVO(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public SearchResult<ProductVOAlpha> getProducts(ProductListParams param) {
        QueryWrapper<Product> wrapper = new QueryWrapper<Product>()
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .in(CollectionUtils.isNotEmpty(param.getCategory()), "category", param.getCategory())
                .orderBy(param.isSort(), param.asc(), param.sortField);
        IPage<Product> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<ProductVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @SneakyThrows
    @Transactional
    public ProductDetailVO getDetail(ProductDetailQry qry) {
        Product product = getById(qry.getId());
        if(product == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.PRODUCT.getName()));
        return ProductDetailVO.builder()
                .item(VOMapper.toVO(product))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .itemImageInfo(CommonImageUtil.segmentImages(product.getImages(), 100, Entity.PRODUCT, true))
                .episodes(getEpisodes(product.getId(), product.getCategory()))
                .build();
    }

    @Transactional
    public void deleteProducts(List<Long> ids) {
        //get original data
        List<Product> items = mapper.selectBatchIds(ids);
        for (Product item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, item.getId());
        }
        //delete
        mapper.delete(new LambdaQueryWrapper<Product>().in(Product::getId, ids));
        //delete person relation
        relationMapper.delete(new LambdaQueryWrapper<PersonRelation>().eq(PersonRelation::getEntityType, ENTITY_VALUE).in(PersonRelation::getEntityId, ids));
    }

    //endregion

    //region other

    private List<EpisodeVOAlpha> getEpisodes(long productId, ProductCategory category) {
        List<EpisodeVOAlpha> res = new ArrayList<>();
        if(Arrays.asList(VIDEO_PRODUCT).contains(category)) {
            List<Episode> eps = epMapper.selectList(new LambdaQueryWrapper<Episode>().eq(Episode::getRelatedId, productId));
            res.addAll(epVOMapper.toVOAlpha(eps));
        }
        return res;
    }

    //endregion
}
