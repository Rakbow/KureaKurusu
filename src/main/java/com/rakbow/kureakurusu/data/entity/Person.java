package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.handler.ImageHandler;
import com.rakbow.kureakurusu.util.handler.LinkHandler;
import com.rakbow.kureakurusu.util.handler.StrListHandler;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-06 3:59
 */
@Data
@TableName(value = "person", autoResultMap = true)
public class Person {

    private Long id; //主键
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name; //原名
    private String nameZh; //简体中文名
    private String nameEn; //英文名
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases; //别名 json数组
    private String cover; //头像url
    private Gender gender; //性别 0-未知 1-男 2-女
    private String birthDate; //生日
    private String detail; //简介
    @TableField(typeHandler = LinkHandler.class)
    private List<Link> links; //链接 json数组
    @TableField(typeHandler = ImageHandler.class)
    private List<Image> images; //图片合集 json数组
    private String remark; //备注
    private String info; //其他信息 json对象
    private Timestamp addedTime; //录入时间
    private Timestamp editedTime; //编辑时间
    private int status; //状态

    public Person() {
        id = 0L;
        name = "";
        nameZh = "";
        nameEn = "";
        aliases = new ArrayList<>();
        cover = "";
        gender = Gender.UNKNOWN;
        birthDate = "";
        detail = "";
        links = new ArrayList<>();
        images = new ArrayList<>();
        remark = "";
        info = "{}";
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = 1;
    }

}
