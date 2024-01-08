package com.rakbow.kureakurusu.data.emun.common;

import com.rakbow.kureakurusu.util.I18nHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Rakbow
 * @since 2022-08-13 22:16
 */
@Getter
@AllArgsConstructor
public enum Entity {

    ENTRY(0, "enum.entity.entry", "entry"),
    ALBUM(1, "enum.entity.album","album"),
    BOOK(2, "enum.entity.book","book"),
    DISC(3, "enum.entity.disc","disc"),
    GAME(4, "enum.entity.game","game"),
    MUSIC(5, "enum.entity.music","music"),
    GOODS(6, "enum.entity.goods","goods"),
    FIGURE(7, "enum.entity.figure","figure"),
    PERSON(8, "enum.entity.person","person"),
    ROLE(90, "enum.entity.role", "person_role");

    private final Integer value;
    private final String labelKey;
    private final String tableName;

    public String getName() {
        return I18nHelper.getMessage(this.labelKey);
    }

    public static String getTableName(int value) {
        return Arrays.stream(Entity.values())
                .filter(entity -> entity.getValue() == value)
                .findFirst()
                .map(Entity::getTableName)
                .orElse(null);
    }
}
