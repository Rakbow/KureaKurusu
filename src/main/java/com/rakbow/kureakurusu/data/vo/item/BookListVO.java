package com.rakbow.kureakurusu.data.vo.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/6 11:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookListVO extends ItemListVO {

    private int pages;

}