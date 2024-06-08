package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/22 11:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumListQueryDTO extends ItemListQueryDTO {

    @QueryColumn
    private String catalogNo;
    @QueryColumn(type = QueryColumnType.NUMBER_LIST)
    private List<Integer> albumFormat;
    @QueryColumn(type = QueryColumnType.NUMBER_LIST)
    private List<Integer> mediaFormat;

    public AlbumListQueryDTO(ListQueryDTO dto) {
        super(dto);
        super.setType(ItemType.ALBUM.getValue());
        this.catalogNo = dto.getVal("catalogNo");
        this.albumFormat = dto.getArray("albumFormat");
        this.mediaFormat = dto.getArray("mediaFormat");
    }

}