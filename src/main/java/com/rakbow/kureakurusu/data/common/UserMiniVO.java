package com.rakbow.kureakurusu.data.common;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-08-02 14:34
 */
@Data
public class UserMiniVO {

    public long id;
    public String username;
    public String avatar;
    public int type;

}
