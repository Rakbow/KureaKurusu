package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

/**
 * VO 存储到搜索引擎数据库
 *
 * @author Rakbow
 * @since 2023-02-03 16:13
 */
@Data
@RedisHash("album")
public class AlbumVOGamma {

    //基础信息
    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameEn;
    private String nameZh;
    private String releaseDate;//发行日期
    private boolean hasBonus;//是否包含特典内容

    //关联信息
    private List<Attribute<Integer>> franchises;//所属系列
    private List<Attribute<Integer>> products;//所属产品id 在mysql中以数组字符串形式存储

    private List<Attribute<Integer>> albumFormat;//专辑分类 在mysql中以数组字符串形式存储

    //图片相关
    private String cover;

    private long visitCount;//浏览数
    private long likeCount;//点赞数

}
