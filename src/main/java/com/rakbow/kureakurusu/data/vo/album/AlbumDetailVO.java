package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Audio;
import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/01/07 1:25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlbumDetailVO {

    private AlbumVO item;
    private List<Audio> audios;

    private PageTraffic traffic;
    private segmentImagesResult itemImageInfo;
    private PersonnelStruct personnel;
    private Map<String, Object> options;

}
