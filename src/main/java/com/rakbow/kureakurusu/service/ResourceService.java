package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ImageListParams;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.ImageDisplayVO;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.DataSorter;
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

        //update item image redis cache
        if (entityType == EntityType.ITEM.getValue()) {
            resetItemImageRedisCache(entityType, entityId);
        }
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

    public String getItemImageCache(long itemId, ImageType imageType) {
        String url = redisUtil.get(
                STR."\{RedisKey.ENTITY_IMAGE_CACHE}\{imageType.getValue()}:\{EntityType.ITEM.getValue()}:\{itemId}"
                , String.class);
        return CommonImageUtil.getItemImage(imageType.getValue(), url);
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

    @SneakyThrows
    public void resetItemImageRedisCache(int entityType, long entityId) {
        String key = STR."\{RedisKey.ENTITY_IMAGE_CACHE}%s:\{entityType}:\{entityId}";
        List<Image> images = imageMapper.selectList(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .in(Image::getType, defaultImageCoverType)
        );
        String defaultCover = CommonConstant.EMPTY_IMAGE_URL;
        images.sort(DataSorter.imageEntityTypeEntityIdTypeSorter);
        Image cover = DataFinder.findImageByEntityTypeEntityIdType(entityType, entityId, ImageType.MAIN.getValue(), images);
        redisUtil.set(String.format(key, ImageType.MAIN.getValue()), cover != null ? cover.getUrl() : defaultCover);
        Image thumb = DataFinder.findImageByEntityTypeEntityIdType(entityType, entityId, ImageType.THUMB.getValue(), images);
        redisUtil.set(String.format(key, ImageType.THUMB.getValue()), thumb != null ? thumb.getUrl() : defaultCover);
    }

}
