package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EntityRelationMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.RelationDTO;
import com.rakbow.kureakurusu.data.dto.RelationManageCmd;
import com.rakbow.kureakurusu.data.dto.RelationQry;
import com.rakbow.kureakurusu.data.emun.DataActionType;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.EntityRelation;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.relation.RelatedItemVO;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2024/2/28 15:19
 */
@Service
@RequiredArgsConstructor
public class RelationService extends ServiceImpl<EntityRelationMapper, EntityRelation> {

    private final EntityRelationMapper mapper;
    private final ItemMapper itemMapper;
    private final ProductMapper productMapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final ResourceService resourceSrv;

    @Transactional
    public List<RelatedItemVO> getRelations(RelationQry qry) {
        List<RelatedItemVO> res = new ArrayList<>();
        //get original relations
        List<EntityRelation> relations = mapper.selectList(
                new LambdaQueryWrapper<EntityRelation>()
                        .eq(EntityRelation::getEntityType, qry.getEntityType())
                        .eq(EntityRelation::getEntityId, qry.getEntityId())
                        .eq(EntityRelation::getStatus, 1)
        );
        if(relations.isEmpty()) return res;
        //relations group by entity type
        Map<Integer, List<EntityRelation>> relationGroup = relations.stream()
                .collect(Collectors.groupingBy(EntityRelation::getRelatedEntityType));
        for(int entity : relationGroup.keySet()) {
            List<EntityRelation> currentRelations = relationGroup.get(entity);
            List<Long> ids = currentRelations.stream().map(EntityRelation::getRelatedEntityId).toList();
            if(entity == EntityType.ITEM.getValue()) {
                List<Item> items = itemMapper.selectList(new LambdaQueryWrapper<Item>().eq(Item::getStatus, 1).in(Item::getId, ids));
                for (EntityRelation r : currentRelations) {
                    RelatedItemVO vo = new RelatedItemVO();
                    vo.setId(r.getId());
                    vo.setEntityType(new Attribute<>(I18nHelper.getMessage(EntityType.ITEM.getLabelKey()), EntityType.ITEM.getValue()));
                    vo.setEntityId(r.getRelatedEntityId());
                    Item item = DataFinder.findItemById(r.getRelatedEntityId(), items);
                    if(item == null) continue;
                    vo.setCover(resourceSrv.getThumbCover(EntityType.ITEM, item.getId()));
                    vo.setName(item.getName());
                    vo.setNameZh(item.getNameZh());
                    vo.setNameEn(item.getNameEn());
                    vo.setLabel(I18nHelper.getMessage(item.getType().getLabelKey()));
                    vo.setRelationType(new Attribute<>(I18nHelper.getMessage(r.getRelatedType().getLabelKey()), r.getRelatedType().getValue()));
                    res.add(vo);
                }
            }
            if(entity == EntityType.PRODUCT.getValue()) {
                List<Product> items = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1).in(Product::getId, ids));
                for (EntityRelation r : currentRelations) {
                    RelatedItemVO vo = new RelatedItemVO();
                    vo.setId(r.getId());
                    vo.setEntityType(new Attribute<>(I18nHelper.getMessage(EntityType.PRODUCT.getLabelKey()), EntityType.PRODUCT.getValue()));
                    vo.setEntityId(r.getRelatedEntityId());
                    Product item = DataFinder.findProductById(r.getRelatedEntityId(), items);
                    if(item == null) continue;
                    vo.setCover(resourceSrv.getThumbCover(EntityType.PRODUCT, item.getId()));
                    vo.setName(item.getName());
                    vo.setNameZh(item.getNameZh());
                    vo.setNameEn(item.getNameEn());
                    vo.setLabel(I18nHelper.getMessage(item.getCategory().getLabelKey()));
                    vo.setRelationType(new Attribute<>(I18nHelper.getMessage(r.getRelatedType().getLabelKey()), r.getRelatedType().getValue()));
                    res.add(vo);
                }
            }
        }
        return res;
    }

    @Transactional
    public void updateRelation(RelationDTO dto) {
        update(
                new LambdaUpdateWrapper<EntityRelation>()
                        .eq(EntityRelation::getId, dto.getId())
                        .set(EntityRelation::getRelatedType, dto.getRelatedType())
                        .set(EntityRelation::getEditedTime, DateHelper.now())
        );
    }

    @Transactional
    public void deleteRelations(List<Long> ids) {
        //delete related episode
        mapper.delete(new LambdaQueryWrapper<EntityRelation>().in(EntityRelation::getId, ids));
    }

    @Transactional
    public void manageRelation(RelationManageCmd cmd) {

        List<EntityRelation> addRelationSet = new ArrayList<>();
        List<EntityRelation> deleteRelationSet = new ArrayList<>();

        cmd.getRelations().forEach(pair -> {
            if(pair.getAction() == DataActionType.INSERT.getValue())
                addRelationSet.add(new EntityRelation(pair, cmd.getEntityType(), cmd.getEntityId()));
            if(pair.getAction() == DataActionType.REAL_DELETE.getValue())
                deleteRelationSet.add(new EntityRelation(pair, cmd.getEntityType(), cmd.getEntityId()));
        });
        //批量删除和批量新增
        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelationSet);
        MybatisBatch<EntityRelation> batchDelete = new MybatisBatch<>(sqlSessionFactory, deleteRelationSet);
        if(!addRelationSet.isEmpty())
            batchInsert.execute(method.insert());
        if(!deleteRelationSet.isEmpty())
            batchDelete.execute(method.deleteById());
    }

}
