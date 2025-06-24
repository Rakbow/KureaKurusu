package com.rakbow.kureakurusu.data.vo.item;

import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/01/08 17:35
 */
@Builder
@Data
public class AlbumTrackVO {

    private long id;
    private long itemId;
    private int serial;
    private String name;
    private String duration;

}
