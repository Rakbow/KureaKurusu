package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.AlbumMapper;
import com.rakbow.kureakurusu.dao.EntityRelationMapper;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.dto.relation.RelationDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.EntityRelation;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.relation.RelatedItemVO;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<RelatedItemVO> getRelations(int entityType, long entityId) {
        List<RelatedItemVO> res = new ArrayList<>();
        //get original relations
        List<EntityRelation> relations = mapper.selectList(
                new LambdaQueryWrapper<EntityRelation>()
                        .eq(EntityRelation::getEntityType, entityType)
                        .eq(EntityRelation::getEntityId, entityId)
                        .eq(EntityRelation::getStatus, 1)
        );
        //relations group by entity type
        Map<Integer, List<EntityRelation>> relationGroup = relations.stream()
                .collect(Collectors.groupingBy(EntityRelation::getRelatedEntityType));
        for(int entity : relationGroup.keySet()) {
            List<Long> ids = relationGroup.get(entity).stream().map(EntityRelation::getRelatedEntityId).toList();
            if(entity == Entity.ALBUM.getValue()) {
                List<Album> items = albumMapper.selectList(new LambdaQueryWrapper<Album>().eq(Album::getStatus, 1).in(Album::getId, ids));
                for (Album item : items) {
                    RelatedItemVO vo = new RelatedItemVO();
                    vo.setEntityType(Entity.ALBUM.getValue());
                    vo.setEntityTypeName(Entity.ALBUM.getTableName());
                    vo.setEntityId(item.getId());
                    vo.setCover(CommonImageUtil.getThumbCoverUrl(item.getImages()));
                    vo.setName(item.getName());
                    vo.setNameZh(item.getNameZh());
                    vo.setNameEn(item.getNameEn());
                    res.add(vo);
                }
            }
            if(entity == Entity.PRODUCT.getValue()) {
                List<Product> items = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1).in(Product::getId, ids));
                for (Product item : items) {
                    RelatedItemVO vo = new RelatedItemVO();
                    vo.setEntityType(Entity.PRODUCT.getValue());
                    vo.setEntityTypeName(Entity.PRODUCT.getTableName());
                    vo.setEntityId(item.getId());
                    vo.setCover(CommonImageUtil.getThumbCoverUrl(item.getImages()));
                    vo.setName(item.getName());
                    vo.setNameZh(item.getNameZh());
                    vo.setNameEn(item.getNameEn());
                    res.add(vo);
                }
            }
        }
        return res;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateRelation(RelationDTO dto) {
        update(
                new LambdaUpdateWrapper<EntityRelation>()
                        .eq(EntityRelation::getId, dto.getId())
                        .set(EntityRelation::getRelatedType, dto.getRelatedType())
                        .set(EntityRelation::getEditedTime, DateHelper.now())
        );
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteRelations(List<Long> ids) {
        //delete related episode
        mapper.delete(new LambdaQueryWrapper<EntityRelation>().in(EntityRelation::getId, ids));
    }

}
