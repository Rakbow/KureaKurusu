package com.rakbow.kureakurusu.data.dto.music;

import com.rakbow.kureakurusu.data.dto.CommonCommand;
import com.rakbow.kureakurusu.data.system.File;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/5 17:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MusicDeleteFileCmd extends CommonCommand {

    private int id;
    private List<File> files;

}
