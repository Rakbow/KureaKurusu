package com.rakbow.kureakurusu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import org.apache.ibatis.type.EnumTypeHandler;

import java.sql.Timestamp;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 3:59
 * @Description:
 */
@Data
@TableName("person")
public class Person {

    private Long id; //主键
    private String name; //原名
    private String nameZh; //简体中文名
    private String nameEn; //英文名
    private String aliases; //别名 json数组
    private String cover; //头像url
    @TableField(typeHandler = EnumTypeHandler.class)
    private Gender gender; //性别 0-未知 1-男 2-女
    private String birthDate; //生日
    private String detail; //简介
    private String links; //链接 json数组
    private String images; //图片合集 json数组
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
        aliases = "";
        cover = "";
        gender = Gender.UNKNOWN;
        birthDate = "";
        detail = "";
        links = "";
        images = "";
        remark = "";
        info = "";
        addedTime = DateHelper.NOW_TIMESTAMP;
        editedTime = DateHelper.NOW_TIMESTAMP;
        status = 1;
    }

}
