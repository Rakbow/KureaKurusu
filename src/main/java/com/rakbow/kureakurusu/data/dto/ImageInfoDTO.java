package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.emun.ImageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/24 16:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageInfoDTO extends DTO {

    private long id;
    private String name;
    private String nameZh;
    private int type;

    public ImageInfoDTO() {
        id = 0L;
        name = "";
        nameZh = "";
        type = ImageType.DEFAULT.getValue();
    }

}
