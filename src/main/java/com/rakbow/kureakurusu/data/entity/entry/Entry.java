package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-02 3:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Entry extends Entity {

    private Long id = 0L;
    @NotBlank(message = "{entity.crud.name.required_field}")
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> links;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String cover;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String thumb;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String detail;

    public Entry() {
        this.setId(0L);
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.aliases = new ArrayList<>();
        this.links = new ArrayList<>();
        this.detail = "";
        this.setRemark("");
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);
    }

}
