package com.rakbow.kureakurusu.data.vo;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/8 7:46
 */
@Data
public class EntityListVO {

    private long id;
    private String name;
    private String remark;
    private String addedTime;
    private String editedTime;
    private boolean status;

}
