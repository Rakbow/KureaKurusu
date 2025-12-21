package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/24 22:44
 */
@Data
public class FavListItemListQueryDTO {

    private long listId;
    private int type;
    private ListQueryDTO param;

}
