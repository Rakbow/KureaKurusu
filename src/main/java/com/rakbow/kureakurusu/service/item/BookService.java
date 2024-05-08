package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ItemBookMapper;
import com.rakbow.kureakurusu.data.entity.ItemBook;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.BookUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2022-12-28 23:45 book业务层
 */
@Service
@RequiredArgsConstructor
public class BookService extends ServiceImpl<ItemBookMapper, ItemBook> {

}
