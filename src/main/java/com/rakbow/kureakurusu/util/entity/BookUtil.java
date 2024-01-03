package com.rakbow.kureakurusu.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-29 20:47
 */
public class BookUtil {

    /**
     * ISBN-10转ISBN-13
     *
     * @param isbn10，isbn书号(10位)
     * @return ISBN-13
     * @author rakbow
     */
    public static String getISBN13(String isbn10) {

        if (isbn10.length() != 10) {
            return isbn10;
        }
        String isbn13 = "978" + isbn10.substring(0, isbn10.length() - 1);
        int a = 0;
        int b = 0;
        int c;
        int d;
        for (int i = 0; i < isbn13.length(); i++) {
            int x = Integer.parseInt(isbn13.substring(i, i + 1));
            if (i % 2 == 0) {
                a += x;
            } else {
                b += x;
            }
        }
        c = a + 3 * b;
        d = 10 - c % 10;
        isbn13 += d;
        return isbn13;
    }

    /**
     * ISBN-13转ISBN-10
     *
     * @param isbn13，isbn书号(13位)
     * @return ISBN-10
     * @author rakbow
     */
    public static String getISBN10(String isbn13) {

        if (isbn13.length() != 13) {
            return isbn13;
        }

        String isbn10 = isbn13.substring(3, 12);

        int isbnSum = 0;
        int tmp = 10;

        for (int i = 3; i < isbn13.length() - 1; i++) {
            isbnSum += Integer.parseInt(isbn13.substring(i, i + 1)) * tmp;
            tmp--;
        }

        int tmp_num = 11 - isbnSum % 11;

        if (tmp_num == 10) {
            isbn10 += "X";
        } else if (tmp_num == 11) {
            isbn10 += 0;
        } else {
            isbn10 += tmp_num;
        }
        return isbn10;
    }

    /**
     * 获取图书信息中的作者
     * @author rakbow
     * @param book 图书
     * */
    public static List<String> getAuthors(Book book) {
        List<String> res = new ArrayList<>();
        JSONArray authors = JSON.parseArray(book.getAuthors());
        if (authors.size() != 0) {
            for (int i = 0; i < authors.size(); i++) {
                JSONObject author = authors.getJSONObject(i);
                if(author.getIntValue("main") == 1) {
                    List<String> authorNames = author.getList("name", String.class);
                    if(authorNames.size() > 0 && authorNames.size() <= 2) {
                        res.addAll(authorNames);
                    }
                    if(authorNames.size() > 2) {
                        res.addAll(authorNames.subList(0, 2));
                    }
                }
            }
        }
        return res;
    }

}
