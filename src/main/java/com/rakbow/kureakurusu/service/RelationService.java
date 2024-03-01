package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.AlbumMapper;
import com.rakbow.kureakurusu.dao.EntityRelationMapper;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.relation.RelationDTO;
import com.rakbow.kureakurusu.data.dto.relation.RelationManageCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.EntityRelation;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.relation.RelatedItemVO;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
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
    private final AlbumMapper albumMapper;
    private final ProductMapper productMapper;
    private final SqlSessionFactory sqlSessionFactory;

    @Transactional
    public List<RelatedItemVO> getRelations(int entityType, long entityId) {
        List<RelatedItemVO> res = new ArrayList<>();
        //get original relations
        List<EntityRelation> relations = mapper.selectList(
                new LambdaQueryWrapper<EntityRelation>()
                        .eq(EntityRelation::getEntityType, entityType)
                        .eq(EntityRelation::getEntityId, entityId)
                        .eq(EntityRelation::getStatus, 1)
        );
        if(relations.isEmpty()) return res;
        //relations group by entity type
        Map<Integer, List<EntityRelation>> relationGroup = relations.stream()
                .collect(Collectors.groupingBy(EntityRelation::getRelatedEntityType));
        for(int entity : relationGroup.keySet()) {
            List<EntityRelation> currentRelations = relationGroup.get(entity);
            List<Long> ids = currentRelations.stream().map(EntityRelation::getRelatedEntityId).toList();
            if(entity == Entity.ALBUM.getValue()) {
                List<Album> items = albumMapper.selectList(new LambdaQueryWrapper<Album>().eq(Album::getStatus, 1).in(Album::getId, ids));
                for (EntityRelation r : currentRelations) {
                    RelatedItemVO vo = new RelatedItemVO();
                    vo.setId(r.getId());
                    vo.setEntityType(new Attribute<>(Entity.ALBUM.getName(), Entity.ALBUM.getValue()));
                    vo.setEntityTypeName(Entity.ALBUM.getTableName());
                    vo.setEntityId(r.getRelatedEntityId());
                    Album item = DataFinder.findAlbumById(r.getRelatedEntityId(), items);
                    if(item == null) continue;
                    vo.setCover(CommonImageUtil.getThumbCoverUrl(item.getImages()));
                    vo.setName(item.getName());
                    vo.setNameZh(item.getNameZh());
                    vo.setNameEn(item.getNameEn());
                    vo.setRelationType(new Attribute<>(I18nHelper.getMessage(r.getRelatedType().getLabelKey()), r.getRelatedType().getValue()));
                    res.add(vo);
                }
            }
            if(entity == Entity.PRODUCT.getValue()) {
                List<Product> items = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1).in(Product::getId, ids));
                for (EntityRelation r : currentRelations) {
                    RelatedItemVO vo = new RelatedItemVO();
                    vo.setId(r.getId());
                    vo.setEntityType(new Attribute<>(Entity.PRODUCT.getName(), Entity.PRODUCT.getValue()));
                    vo.setEntityTypeName(Entity.PRODUCT.getTableName());
                    vo.setEntityId(r.getRelatedEntityId());
                    Product item = DataFinder.findProductById(r.getRelatedEntityId(), items);
                    if(item == null) continue;
                    vo.setCover(CommonImageUtil.getThumbCoverUrl(item.getImages()));
                    vo.setName(item.getName());
                    vo.setNameZh(item.getNameZh());
                    vo.setNameEn(item.getNameEn());
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
