package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.item.Disc;
import com.rakbow.kureakurusu.data.vo.item.AlbumTrackVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/24 17:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMappers({
        @AutoMapper(target = Disc.class, reverseConvertGenerate = false)
})
public class DiscCreateDTO extends DTO {

    private long id;
    private long itemId;
    private String catalogId;
    private int discNo;
    private Integer mediaFormat;
    private List<Integer> albumFormat;

    private List<AlbumTrackVO> tracks;

}
