package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.BookMapper;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.entity.BookUtil;
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
public class BookService extends ServiceImpl<BookMapper, Book> {

     /**
      * isbn互相转换
      *
      * @param label,isbn 转换方式,isbn
      * @return isbn
      * @author rakbow
      */
     @SneakyThrows
     public String getISBN(String label, String isbn) {

         isbn = isbn.replaceAll("-", "");

         if(StringUtils.equals(label, "isbn13")) {
             if(isbn.length() != 10) {
                 throw new Exception(I18nHelper.getMessage("book.crud.isbn10.invalid"));
             }
             return BookUtil.getISBN13(isbn);
         }
         if(StringUtils.equals(label, "isbn10")) {
             if(isbn.length() != 13) {
                 throw new Exception(I18nHelper.getMessage("book.crud.isbn13.invalid"));
             }
             return BookUtil.getISBN10(isbn);
         }
         return null;
     }

}
