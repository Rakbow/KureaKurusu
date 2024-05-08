package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.DiscMapper;
import com.rakbow.kureakurusu.data.entity.Disc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2022-12-14 23:24 disc业务层
 */
@Service
@RequiredArgsConstructor
public class DiscService extends ServiceImpl<DiscMapper, Disc> {
}