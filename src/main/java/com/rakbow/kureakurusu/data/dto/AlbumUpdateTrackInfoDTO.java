package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.vo.item.DiscVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 1:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumUpdateTrackInfoDTO extends DTO {

    private long id;
    private List<DiscVO> discs;//碟片列表

}
