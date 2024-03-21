package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.vo.album.AlbumDiscVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 1:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumUpdateTrackInfoCmd extends CommonCommand {

    private long id;
    private List<AlbumDiscVO> discs;//碟片列表

}
