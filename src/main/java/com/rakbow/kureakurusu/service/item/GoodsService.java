package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.GoodsMapper;
import com.rakbow.kureakurusu.data.entity.Goods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2023-01-04 14:19
 */
@Service
@RequiredArgsConstructor
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {
}
