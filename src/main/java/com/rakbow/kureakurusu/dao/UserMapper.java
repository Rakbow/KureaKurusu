package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-02 0:46
 * @Description:
 */
@Mapper
public interface UserMapper {

    //通过id查找用户
    User selectUserById(int id);

    //通过用户名查找用户
    User selectUserByUsername(String username);

    //通过邮箱查找用户
    User selectUserByEmail(String email);

    //新作用户
    int insertUser(User user);

    //修改用户状态
    int updateStatus(int id, int status);

    //修改用户头像
    int updateHeader(int id, String headerUrl);

    //修改密码
    int updatePassword(int id, String password);
}
