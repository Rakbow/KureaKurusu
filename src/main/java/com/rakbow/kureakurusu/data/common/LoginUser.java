package com.rakbow.kureakurusu.data.common;

import com.rakbow.kureakurusu.data.entity.User;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-08-02 14:34
 */
@Data
public class LoginUser {

    public long id;
    public String username;
    public String headerUrl;
    public int type;

    public void create(User user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setHeaderUrl(user.getHeaderUrl());
        this.setType(user.getType().getValue());
    }

}
