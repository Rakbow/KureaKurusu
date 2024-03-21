package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/01/10 15:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumTrackInfoQry extends CommonCommand {

    private long id;

}
