package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeListVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVO;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.*;

/**
 * @author Rakbow
 * @since 2024/01/08 14:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "episode", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = EpisodeVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class),
        @AutoMapper(target = EpisodeListVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Episode extends Entity {

    @Builder.Default
    private Long id;//表主键
    private int relatedType;//关联类型 Album Product
    private long relatedId;//关联id 音乐 Album.id 剧集 Product.id
    private String name;//标题(原)
    private String nameEn;//标题(英)
    private String premiereDate;//首播日期
    @AutoMapping(qualifiedByName = "duration")
    private int duration;//时长 单位：秒
    private int serial;//序号
    private String detail;//详情
    private int episodeType;//类型 0-音乐 1-剧集

    public Episode() {
        super();
        id = 0L;
        relatedType = 0;
        relatedId = 0;
        name = "";
        nameEn = "";
        premiereDate = "";
        duration = 0;
        serial = 1;
        detail = "";
        episodeType = 0;
    }

}
