package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.entity.ItemAlbum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/3 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumItemUpdateDTO extends ItemUpdateDTO {

    private String catalogNo;
    @NotEmpty(message = "{entity.crud.publish_format.required_field}")
    private List<Integer> publishFormat;
    @NotEmpty(message = "{entity.crud.album_format.required_field}")
    private List<Integer> albumFormat;
    @NotEmpty(message = "{entity.crud.media_format.required_field}")
    private List<Integer> mediaFormat;
    private boolean hasBonus;

    public ItemAlbum toItemAlbum() {
        ItemAlbum itemAlbum = new ItemAlbum();
        itemAlbum.setId(this.getId());
        itemAlbum.setCatalogNo(this.catalogNo);
        itemAlbum.setPublishFormat(this.publishFormat);
        itemAlbum.setAlbumFormat(this.albumFormat);
        itemAlbum.setMediaFormat(this.mediaFormat);
        itemAlbum.setHasBonus(this.hasBonus);
        itemAlbum.setBonus(null);
        return itemAlbum;
    }

}