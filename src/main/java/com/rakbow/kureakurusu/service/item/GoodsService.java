package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ItemGoodsMapper;
import com.rakbow.kureakurusu.data.entity.item.ItemGoods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2023-01-04 14:19
 */
@Service
@RequiredArgsConstructor
public class GoodsService extends ServiceImpl<ItemGoodsMapper, ItemGoods> {
}
