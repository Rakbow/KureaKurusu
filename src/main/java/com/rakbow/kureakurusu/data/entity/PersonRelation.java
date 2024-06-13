package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
}
