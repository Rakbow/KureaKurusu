package com.rakbow.kureakurusu.data.dto.album;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/01/07 1:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateAlbumTrackInfoCmd extends CommonCommand {

    private long id;
    private String discList;

}
