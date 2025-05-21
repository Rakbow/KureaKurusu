package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.RelatedGroup;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.entry.ProductListVO;
import com.rakbow.kureakurusu.toolkit.DataSorter;
import com.rakbow.kureakurusu.toolkit.VisitUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final RelationMapper relatedMapper;
    private final RelationMapper relationMapper;
    private final VisitUtil visitUtil;
    private final Converter converter;
    //endregion

    //region const
    private final EntityType ENTITY_VALUE = EntityType.PRODUCT;

    //endregion

    @Transactional
    public void deleteProducts(List<Long> ids) {
        //get original data
        List<Product> items = mapper.selectByIds(ids);
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

    @Transactional
    public List<ProductListVO> getSubProducts(long id) {
        List<ProductListVO> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<Relation> relations = relatedMapper.selectList(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRelatedGroup, RelatedGroup.RELATED_PRODUCT)
                        .eq(Relation::getRelatedEntityType, EntityType.PRODUCT)
                        .eq(Relation::getRelatedEntityId, id)
        );
        if (relations.isEmpty())
            return res;
        List<Long> targetIds = relations.stream().map(Relation::getEntityId).distinct().toList();
        List<Product> targets = mapper.selectByIds(targetIds);
        targets.sort(DataSorter.productDateSorter);
        res = converter.convert(targets, ProductListVO.class);
        return res;
    }

    //endregion
}
