package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.ItemBookMapper;
import com.rakbow.kureakurusu.data.entity.item.ItemBook;
import com.rakbow.kureakurusu.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
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
     * isbn互相转换
     *
     * @param label,isbn 转换方式,isbn
     * @return isbn
     * @author rakbow
     */
    @SneakyThrows
    public String getISBN(String label, String isbn) {

        ISBNValidator validator = new ISBNValidator();
        isbn = isbn.replaceAll("-", "");

        if (StringUtils.equals(label, "isbn13")) {
            if (validator.isValidISBN10(isbn))
                throw new ApiException("book.crud.isbn10.invalid");
            return validator.convertToISBN13(isbn);
        } else if (StringUtils.equals(label, "isbn10")) {
            if (validator.isValidISBN13(isbn))
                throw new ApiException("book.crud.isbn13.invalid");
            String isbn10Base = isbn.substring(3, 12);
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (10 - i) * (isbn10Base.charAt(i) - '0');
            }
            int checkDigit = 11 - (sum % 11);
            String checkDigitStr = (checkDigit == 10) ? "X" : (checkDigit == 11) ? "0" : String.valueOf(checkDigit);

            return isbn10Base + checkDigitStr;
        }
        return null;
    }

}