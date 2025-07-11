package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ItemGameMapper;
import com.rakbow.kureakurusu.data.entity.item.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2023-01-06 14:43 game业务层
 */
@Service
@RequiredArgsConstructor
public class GameService extends ServiceImpl<ItemGameMapper, Game> {
}
