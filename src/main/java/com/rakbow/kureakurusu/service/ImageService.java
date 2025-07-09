package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.dto.ImageDeleteDTO;
import com.rakbow.kureakurusu.data.dto.ImageListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.dto.ImageUpdateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.resource.ImageDisplayVO;
import com.rakbow.kureakurusu.data.vo.resource.ImageVO;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/5/26 20:10
 */
@Service
@RequiredArgsConstructor
public class ImageService extends ServiceImpl<ImageMapper, Image> {

    private final QiniuImageUtil qiniuImageUtil;
    private final SqlSessionFactory sqlSessionFactory;
    private final RedisUtil redisUtil;
    private final Converter converter;

    private final static List<Integer> defaultImageType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.DEFAULT.getValue()
    );

    private final static List<Integer> defaultImageCoverType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.THUMB.getValue()
    );

    @Transactional
    public SearchResult<ImageVO> list(ImageListQueryDTO param) {
        MPJLambdaWrapper<Image> wrapper = new MPJLambdaWrapper<Image>()
                .eq(Image::getEntityType, param.getEntityType())
                .eq(Image::getEntityId, param.getEntityId())
                .like(StringUtils.isNotEmpty(param.getKeyword()), Image::getName, param.getKeyword())
                .eq(ObjectUtils.isNotEmpty(param.getType()) && param.getType() != -1 && param.getType() != -2, Image::getType, param.getType())
                .in(ObjectUtils.isNotEmpty(param.getType()) && param.getType() == -2, Image::getType, defaultImageType)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        IPage<Image> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<ImageVO> res = converter.convert(pages.getRecords(), ImageVO.class);
        return new SearchResult<>(res, pages.getTotal());
    }

    @SneakyThrows
    @Transactional
    public void upload(int entityType, long entityId, List<ImageMiniDTO> images, boolean generateThumb) {
        //generate thumb
        if (generateThumb) {
            ImageMiniDTO cover = images.stream().filter(i -> i.getType() == ImageType.MAIN.getValue()).findFirst().orElse(null);
            if (cover != null) {
                ImageMiniDTO thumb = CommonImageUtil.generateThumbTmp(cover);
                images.add(thumb);
            }
        }
        //upload to qiniu server
        List<Image> addImages = qiniuImageUtil.uploadImages(entityType, entityId, images);
        //batch insert
        MybatisBatch.Method<Image> method = new MybatisBatch.Method<>(ImageMapper.class);
        MybatisBatch<Image> batchInsert = new MybatisBatch<>(sqlSessionFactory, addImages);
        batchInsert.execute(method.insert());

        //update item image redis cache
        if (entityType == EntityType.ITEM.getValue()) {
            resetCache(entityType, entityId);
        }
    }

    @Transactional
    public void update(ImageUpdateDTO dto) {
        updateById(converter.convert(dto, Image.class));
    }

    @Transactional
    public void delete(List<ImageDeleteDTO> images) {
        String[] keys = images.stream().map(ImageDeleteDTO::getUrl)
                .map(url -> url.replace(Constant.FILE_DOMAIN, "")).toArray(String[]::new);
        //delete from qiniu server
        List<Integer> deleteIndexes = qiniuImageUtil.deleteImages(keys);
        //delete from database
        removeByIds(deleteIndexes.stream().map(index -> images.get(index).getId()).toList());
    }

    public ImageDisplayVO preview(int entityType, long entityId) {
        IPage<Image> pages = page(new Page<>(1, 6),
                new LambdaQueryWrapper<Image>().eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .in(Image::getType, defaultImageType)
                        .orderByAsc(Image::getId)
        );
        return new ImageDisplayVO(converter.convert(pages.getRecords(), ImageVO.class), pages.getTotal());
    }

    public String getCache(int entityType, long entityId, ImageType imageType) {
        String url = redisUtil.get(
                STR."\{RedisKey.ENTITY_IMAGE_CACHE}\{imageType.getValue()}:\{entityType}:\{entityId}", String.class);
        return CommonImageUtil.getItemImage(imageType.getValue(), url);
    }

    @SneakyThrows
    public void resetCache(int entityType, long entityId) {
        String key = STR."\{RedisKey.ENTITY_IMAGE_CACHE}%s:\{entityType}:\{entityId}";
        List<Image> images = list(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .in(Image::getType, defaultImageCoverType)
        );
        String defaultCover = CommonConstant.EMPTY_IMAGE_URL;
        Image cover = images.stream().filter(i -> i.getType() == ImageType.MAIN).findFirst().orElse(null);
        redisUtil.set(String.format(key, ImageType.MAIN.getValue()), cover != null ? cover.getUrl() : defaultCover);
        Image thumb = images.stream().filter(i -> i.getType() == ImageType.THUMB).findFirst().orElse(null);
        redisUtil.set(String.format(key, ImageType.THUMB.getValue()), thumb != null ? thumb.getUrl() : defaultCover);
    }

    public List<EntityRelatedCount> count(Integer entityType, List<Long> ids) {
        MPJLambdaWrapper<Image> wrapper = new MPJLambdaWrapper<Image>()
                .select(Image::getEntityId)
                .selectCount(Image::getId, "count")
                .eq(Image::getEntityType, entityType)
                .in(Image::getType, defaultImageType)
                .in(Image::getEntityId, ids)
                .groupBy(Image::getEntityType, Image::getEntityId);
        List<Map<String, Object>> maps = listMaps(wrapper);

        List<EntityRelatedCount> res = JsonUtil.to(maps, EntityRelatedCount.class);
        res.forEach(r -> r.setEntityType(entityType));
        return res;
    }

}
