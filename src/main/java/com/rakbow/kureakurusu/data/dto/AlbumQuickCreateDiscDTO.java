package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.dto.DTO;
import com.rakbow.kureakurusu.data.vo.item.AlbumTrackVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/12/23 18:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumQuickCreateDiscDTO extends DTO {

    private long id;
    private int serial;
    private List<AlbumTrackVO> tracks;//碟片列表

}
