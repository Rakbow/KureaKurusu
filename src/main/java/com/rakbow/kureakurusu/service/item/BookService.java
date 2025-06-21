package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ItemBookMapper;
import com.rakbow.kureakurusu.data.entity.item.ItemBook;
import com.rakbow.kureakurusu.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2022-12-28 23:45 book业务层
 */
@Service
@RequiredArgsConstructor
public class BookService extends ServiceImpl<ItemBookMapper, ItemBook> {

    /**
     * convert isbn-10 to isbn-13
     *
     * @param isbn10 isbn10
     * @return isbn13
     * @author rakbow
     */
    @SneakyThrows
    public String convertISBN(String isbn10) {

        ISBNValidator validator = new ISBNValidator();
        isbn10 = isbn10.replaceAll("-", "");
        if (validator.isValidISBN10(isbn10))
            throw new ApiException("book.crud.isbn10.invalid");
        return validator.convertToISBN13(isbn10);
    }

}