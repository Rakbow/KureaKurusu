package com.rakbow.kureakurusu.data.entity.item;


import com.rakbow.kureakurusu.data.vo.item.AlbumListVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumVO;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2022-07-19 0:55 专辑实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AutoMappers({
        @AutoMapper(target = AlbumListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = AlbumVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Album extends SuperItem {

    private int discs;
    private int tracks;
    private int runTime;//second

    public Album() {
        super();
    }
}
