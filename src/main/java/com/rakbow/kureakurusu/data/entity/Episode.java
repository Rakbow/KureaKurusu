package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
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
    private Long id;
    private int relatedType;//Album Product
    private long relatedId;//AlbumDisc.id Product.id
    private String name;
    private String nameEn;
    private String premiereDate;
    @AutoMapping(qualifiedByName = "duration")
    private int duration;//second
    private int serial;
    private String detail;
    private int episodeType;//0-music 1-animation episode

    @TableField(exist = false)
    private int discNo;
    @TableField(exist = false)
    private EntityMiniVO parent = new EntityMiniVO();

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

    public void setParent(Item item) {
        parent.setId(item.getId());
        parent.setName(item.getName());
        parent.setType(EntityType.ITEM.getValue());
    }

}
