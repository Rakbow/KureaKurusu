package com.rakbow.kureakurusu.data.system;

import com.rakbow.kureakurusu.entity.User;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-08-02 14:34
 * @Description:
 */
@Data
public class LoginUser {

    public int id;
    public String username;
    public String headerUrl;
    public int type;

    public void create(User user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setHeaderUrl(user.getHeaderUrl());
        this.setType(user.getType());
    }

}
