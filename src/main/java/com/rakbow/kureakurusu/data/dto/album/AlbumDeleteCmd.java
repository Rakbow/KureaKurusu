package com.rakbow.kureakurusu.data.dto.album;

import com.rakbow.kureakurusu.data.dto.CommonCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/3 16:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumDeleteCmd extends CommonCommand {

    List<Long> ids;

}
