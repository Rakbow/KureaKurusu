package com.rakbow.kureakurusu.data.emun.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-13 22:16
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum Entity {

    ENTRY(0,"Entry", "Entry", "entry"),
    ALBUM(1,"专辑", "Album", "album"),
    BOOK(2,"书籍", "Book", "book"),
    DISC(3,"碟片", "Disc", "disc"),
    GAME(4,"游戏", "Game", "game"),
    MUSIC(5,"音乐", "Music", "music"),
    GOODS(6,"周边", "Goods", "goods"),
    FIGURE(7,"手办", "Figure", "figure"),
    PERSON(8,"人物","Person", "person");

    private final Integer id;
    private final String nameZh;
    private final String nameEn;
    private final String tableName;

    public static String getTableName(int id) {
        String nameEn = Arrays.stream(Entity.values())
                .filter(entity -> entity.getId() == id)
                .findFirst()
                .map(Entity::getNameEn)
                .orElse(null);
        assert nameEn != null;
        return nameEn.toLowerCase();
    }
}
