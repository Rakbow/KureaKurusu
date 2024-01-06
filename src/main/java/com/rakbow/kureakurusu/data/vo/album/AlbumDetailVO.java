package com.rakbow.kureakurusu.data.vo.album;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Audio;
import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.image.ItemDetailInfo;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 1:25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlbumDetailVO {

    private AlbumVO album;
    private List<Audio> audioInfos;
    private ItemDetailInfo detailInfo;
    private PageTraffic pageInfo;
    private segmentImagesResult itemImageInfo;
    private PersonnelStruct personnel;

    private JSONObject options;

}
