package com.rakbow.kureakurusu.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.util.jackson.BooleanToIntDeserializer;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2023-05-02 3:55
 */
@Data
public class MetaEntity {

    @TableField("images")
    private String images;//图片列表（JSON字符串）
    @TableField("description")
    private String description;//描述
    @TableField("remark")
    private String remark;//备注
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    @TableField("added_time")
    private Timestamp addedTime;//数据新增时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    @TableField("edited_time")
    private Timestamp editedTime;//数据更新时间
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    @TableField("status")
    private int status;//激活状态

}
