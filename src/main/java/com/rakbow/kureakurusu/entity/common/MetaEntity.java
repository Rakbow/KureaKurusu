package com.rakbow.kureakurusu.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.util.jackson.BooleanToIntDeserializer;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-02 3:55
 * @Description:
 */
@Data
public class MetaEntity {

    private String images;//图片列表（JSON字符串）
    private String description;//描述
    private String remark;//备注
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Timestamp addedTime;//数据新增时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Timestamp editedTime;//数据更新时间
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private int status;//激活状态

}
