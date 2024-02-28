package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/2/28 17:57
 */
@Data
public class AlbumMiniVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private String cover;
    private String catalogNo;
    private String releaseDate;

}
