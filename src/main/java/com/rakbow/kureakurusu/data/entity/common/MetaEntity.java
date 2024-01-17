package com.rakbow.kureakurusu.data.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.handler.ImageHandler;
import com.rakbow.kureakurusu.util.jackson.BooleanToIntDeserializer;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-02 3:55
 */
@Data
public class MetaEntity {

    @TableField(typeHandler = ImageHandler.class)
    private List<Image> images;//图片列表
    private String detail;//描述
    private String remark;//备注
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime;//数据新增时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime;//数据更新时间
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private int status;//激活状态

}
