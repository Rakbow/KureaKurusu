package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/8 7:09
 */
@Data
public class Entity {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;

    private String remark;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

}
