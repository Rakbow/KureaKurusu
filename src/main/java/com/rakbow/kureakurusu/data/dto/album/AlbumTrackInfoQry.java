package com.rakbow.kureakurusu.data.dto.album;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import com.rakbow.kureakurusu.data.vo.album.AlbumDiscVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/10 15:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumTrackInfoQry extends CommonCommand {

    private long id;

}
