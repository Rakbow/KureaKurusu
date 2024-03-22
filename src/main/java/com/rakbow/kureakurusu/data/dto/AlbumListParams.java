package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/22 11:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumListParams extends QueryParams {

    private String name;
    private String nameZh;
    private String nameEn;
    private String catalogNo;
    private String barcode;
    private List<Integer> albumFormat;
    private List<Integer> mediaFormat;
    private List<Integer> publishFormat;

    public AlbumListParams(ListQry qry) {
        super(qry);
        this.name = super.getStr("name");
        this.nameZh = super.getStr("nameZh");
        this.nameEn = super.getStr("nameEn");
        this.catalogNo = super.getStr("catalogNo");
        this.barcode = super.getStr("barcode");
        this.albumFormat = super.getArray("albumFormat");
        this.mediaFormat = super.getArray("mediaFormat");
        this.publishFormat = super.getArray("publishFormat");
    }

}
