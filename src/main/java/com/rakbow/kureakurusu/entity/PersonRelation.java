package com.rakbow.kureakurusu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import lombok.Data;
import org.apache.ibatis.type.EnumTypeHandler;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-12 23:40
 * @Description:
 */
@Data
@TableName("person_relation")
public class PersonRelation {

    @TableId
    private Long id;
    private long personId;
    private long roleId;
    @TableField(typeHandler = EnumTypeHandler.class)
    private Entity entityType;
    private long entityId;

    public PersonRelation() {
        id = 0L;
        personId = 0;
        roleId = 0;
        entityType = Entity.ALBUM;
        entityId = 0;
    }
}
