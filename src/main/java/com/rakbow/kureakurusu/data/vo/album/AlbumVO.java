package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.ItemVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumVO extends ItemVO {

    private String catalogNo;//专辑编号
    private String releaseDate;//发行日期
    private boolean hasBonus;//是否包含特典内容

    //规格信息
    private List<Attribute<Integer>> publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private List<Attribute<Integer>> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private List<Attribute<Integer>> mediaFormat;//媒体类型

    //大文本字段
    private String bonus;//特典信息

}
