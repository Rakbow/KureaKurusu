package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Audio;
import com.rakbow.kureakurusu.data.vo.ItemDetailVO;
import lombok.*;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 1:25
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlbumDetailVO extends ItemDetailVO {

    private List<Audio> audios;
    private AlbumTrackInfoVO trackInfo;

}
