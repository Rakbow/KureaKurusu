package com.rakbow.kureakurusu.data.emun.entity.book;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.rakbow.kureakurusu.data.emun.entity.album.AlbumFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-12-29 21:29
 */
@Getter
@AllArgsConstructor
public enum BookType {

    UNCATEGORIZED(0,"enum.book_type.uncategorized"),
    NOVEL(1,"enum.book_type.novel"),
    COMIC(2,"enum.book_type.manga"),
    ANTHOLOGY(3,"enum.book_type.anthology"),
    ART_BOOK(4,"enum.book_type.art_book"),
    ELECTRONIC_BOOK(5,"enum.book_type.e_book"),
    OTHER(6,"enum.book_type.other");

    @EnumValue
    private final int value;
    private final String labelKey;

    public static BookType get(int value) {
        for (BookType type : BookType.values()) {
            if(type.value == value)
                return type;
        }
        return UNCATEGORIZED;
    }

}
