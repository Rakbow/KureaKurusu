package com.rakbow.kureakurusu.data.dto.album;

import com.rakbow.kureakurusu.data.dto.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/3 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumUpdateDTO extends DTO {

    private long id;
    private String catalogNo;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private String barcode;
    @NotBlank(message = "{entity.crud.release_date.required_field}")
    private String releaseDate;
    @NotEmpty(message = "{entity.crud.publish_format.required_field}")
    private List<Integer> publishFormat;
    @NotEmpty(message = "{entity.crud.album_format.required_field}")
    private List<Integer> albumFormat;
    @NotEmpty(message = "{entity.crud.media_format.required_field}")
    private List<Integer> mediaFormat;

    private int price;
    private String currencyUnit;
    private int hasBonus;
    private String remark;
}
