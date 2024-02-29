package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.dto.relation.RelationDTO;
import com.rakbow.kureakurusu.data.emun.common.RelatedType;
import com.rakbow.kureakurusu.data.relation.RelationPair;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * Entity关系
 *
 * @author Rakbow
 * @since 2024/2/28 11:43
 */
@Data
@ToString(callSuper = true)
@TableName(value = "entity_relation", autoResultMap = true)
public class EntityRelation {

    private Long id;
    private int entityType;
    private long entityId;
    private int relatedEntityType;
    private long relatedEntityId;
    private RelatedType relatedType;
    private Timestamp addedTime;
    private Timestamp editedTime;
    private Boolean status;

    public EntityRelation() {
        id = 0L;
        entityType = 0;
        entityId = 0;
        relatedEntityType = 0;
        relatedEntityId = 0;
        relatedType = RelatedType.OTHER;
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = true;
    }

    public EntityRelation(RelationDTO dto) {
        id = dto.getId();
        entityType = dto.getEntityType();
        entityId = dto.getEntityId();
        relatedEntityType = dto.getRelatedEntityType();
        relatedEntityId = dto.getRelatedEntityId();
        relatedType = RelatedType.get(dto.getRelatedType());
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = true;
    }

    public EntityRelation(RelationPair pair, int entityType, long entityId) {
        id = pair.getId();
        this.entityType = entityType;
        this.entityId = entityId;
        this.relatedEntityType = pair.getEntityType();
        this.relatedEntityId = pair.getEntityId();
        this.relatedType = RelatedType.get(pair.getType().getValue());
        this.addedTime = DateHelper.now();
        this.editedTime = DateHelper.now();
        this.status = true;
    }

}
