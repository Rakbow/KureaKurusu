package com.rakbow.kureakurusu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.person.PersonnelPair;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-12-12 23:40
 */
@Data
@TableName("person_relation")
public class PersonRelation {

    @TableId
    private Long id;
    private long personId;
    private long roleId;
    // @TableField(typeHandler = EnumTypeHandler.class)
    // private Entity entityType;
    private int entityType;
    private long entityId;
    private int main;

    public PersonRelation() {
        id = 0L;
        personId = 0;
        roleId = 0;
        entityType = 0;
        entityId = 0;
        main = 0;
    }

    public PersonRelation(PersonnelPair pair, int entityType, long entityId) {
        id = pair.getId();
        this.personId = pair.getPerson().getValue() != null ? pair.getPerson().getValue() : 0;
        this.roleId = pair.getRole().getValue() != null ? pair.getRole().getValue() : 0;
        this.entityType = entityType;
        this.entityId = entityId;
        this.main = pair.isMain();
    }

    public boolean mainFlag() {
        return this.main == 1;
    }
}
