package com.rakbow.kureakurusu.data.vo.temp;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/12/18 21:02
 */
@Data
public abstract class EntitySearchVO {

    private long id;
    private int type;
    private int subType;
    private String name;
    private String thumb;
    private String cover;
    private Timestamp createdAt;
    private Object parent;

}
