package com.rakbow.kureakurusu.data.episode;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import com.rakbow.kureakurusu.data.system.File;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/26 14:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeDeleteCmd extends CommonCommand {

    private int id;
    private List<File> files;

}
