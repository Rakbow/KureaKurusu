package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ImageListParams;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.ImageDisplayVO;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/26 20:10
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ImageMapper imageMapper;
    private final QiniuImageUtil qiniuImageUtil;
    private final SqlSessionFactory sqlSessionFactory;
    private final RedisUtil redisUtil;

    private final static List<Integer> defaultImageType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.DEFAULT.getValue()
    );

    private final static List<Integer> defaultImageCoverType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.THUMB.getValue()
    );

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @author rakbow
     */
    @Transactional
    public SearchResult<Image> getEntityImages(ImageListParams param) {
        QueryWrapper<Image> wrapper = new QueryWrapper<Image>()
                .eq("entity_type", param.getEntityType())
                .eq("entity_id", param.getEntityId())
                .eq(param.getType() != null && param.getType() != -1 && param.getType() != -2, "type", param.getType())
                .in(param.getType() != null && param.getType() == -2, "type", defaultImageType)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        IPage<Image> pages = imageMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        CommonImageUtil.generateThumb(pages.getRecords());
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
    }

    @SneakyThrows
    @Transactional
    public void addEntityImage(int entityType, long entityId, List<ImageMiniDTO> images) {
        //upload to qiniu server
        List<Image> addImages = qiniuImageUtil.commonAddImages(entityType, entityId, images);
        //batch insert
        MybatisBatch.Method<Image> method = new MybatisBatch.Method<>(ImageMapper.class);
        MybatisBatch<Image> batchInsert = new MybatisBatch<>(sqlSessionFactory, addImages);
        batchInsert.execute(method.insert());
    }

    /**
     * 更新图片
     *
     * @author rakbow
     */
    @Transactional
    public void updateEntityImage(Image image) {
        imageMapper.updateById(image);
    }

    /**
     * delete image
     *
     * @author rakbow
     */
    @Transactional
    public void deleteEntityImage(List<Image> images) {
        //delete from qiniu server
        List<Image> deleteImages = qiniuImageUtil.deleteImage(images);
        //delete from database
        imageMapper.deleteByIds(deleteImages.stream().map(Image::getId).toList());
    }

    public String getItemCover(ItemType type, long itemId) {
        String cover = getCoverRedisCache(EntityType.ITEM.getValue(), itemId);
        return CommonImageUtil.getItemCover(type, cover);
    }

    public String getThumbCover(int entityType, long entityId) {
        Image cover = imageMapper.selectOne(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .eq(Image::getType, ImageType.THUMB)
        );
        return CommonImageUtil.getThumbCover(cover);
    }

    public ImageDisplayVO getEntityDisplayImages(int entityType, long entityId) {
        QueryWrapper<Image> wrapper = new QueryWrapper<Image>()
                .eq("entity_type", entityType)
                .eq("entity_id", entityId)
                .in("type", defaultImageType)
                .orderByAsc("id");
        IPage<Image> pages = imageMapper.selectPage(new Page<>(1, 6), wrapper);
        CommonImageUtil.generateThumb(pages.getRecords());
        return new ImageDisplayVO(pages.getRecords(), imageMapper.selectCount(wrapper).intValue());
    }

    public List<Image> getItemThumbAndCover(List<Long> ids) {
        return imageMapper.selectList(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, EntityType.ITEM.getValue())
                        .in(Image::getEntityId, ids)
                        .in(Image::getType, defaultImageCoverType)
        );
    }

    @Transactional
    public List<Image> getEntityThumbs(int entityType, List<Long> entityIds) {
        return imageMapper.selectList(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, entityType)
                        .in(Image::getEntityId, entityIds)
                        .in(Image::getType, ImageType.THUMB)
        );
    }

    public String getCoverRedisCache(int entityType, long entityId) {
        String key = STR."entity_image_cache:\{ImageType.MAIN.getValue()}:\{entityType}:\{entityId}";
        if (redisUtil.hasKey(key)) return redisUtil.get(key, String.class);
        Image image = imageMapper.selectOne(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .eq(Image::getType, ImageType.MAIN)
        );
        String cover = CommonConstant.EMPTY_IMAGE_URL;
        if(image != null) cover = image.getUrl();
        redisUtil.set(key, cover);
        return cover;
    }

}
