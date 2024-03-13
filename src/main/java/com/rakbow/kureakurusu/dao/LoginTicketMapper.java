package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author Rakbow
 * @since 2022-08-17 0:24
 */
@Mapper
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {
}
