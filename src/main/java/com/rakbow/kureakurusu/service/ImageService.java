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
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.enums.ChangelogField;
import com.rakbow.kureakurusu.data.enums.ChangelogOperate;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.ImageType;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.resource.ImageDisplayVO;
import com.rakbow.kureakurusu.data.vo.resource.ImageVO;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final ChangelogService logSrv;

    private final static List<Integer> defaultImageType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.DEFAULT.getValue()
    );

    private final static List<Integer> defaultImageCoverType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.THUMB.getValue()
    );

    @Transactional
    public SearchResult<ImageVO> list(ImageListQueryDTO dto) {
        MPJLambdaWrapper<Image> wrapper = new MPJLambdaWrapper<Image>()
                .eq(Image::getEntityType, dto.getEntityType())
                .eq(Image::getEntityId, dto.getEntityId())
                .like(StringUtil.isNotEmpty(dto.getKeyword()), Image::getName, dto.getKeyword())
                .eq(Objects.nonNull(dto.getType()) && dto.getType() != -1 && dto.getType() != -2, Image::getType, dto.getType())
                .in(Objects.nonNull(dto.getType()) && dto.getType() == -2, Image::getType, defaultImageType)
                .orderByAsc(!dto.isSort(), Image::getIdx)
                .orderBy(dto.isSort(), dto.asc(), dto.getSortField());
        IPage<Image> pages = page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
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

        logSrv.create(entityType, entityId, ChangelogField.IMAGE, ChangelogOperate.UPLOAD);
    }

    @Transactional
    public void update(ImageUpdateDTO dto) {
        updateById(converter.convert(dto, Image.class));
    }

    @Transactional
    public void delete(ImageDeleteDTO dto) {
        String[] keys = dto.getImages().stream().map(ImageDeleteMiniDTO::getUrl)
                .map(url -> url.replace(Constant.FILE_DOMAIN, "")).toArray(String[]::new);
        //delete from qiniu server
        List<Integer> deleteIndexes = qiniuImageUtil.deleteImages(keys);
        //delete from database
        removeByIds(deleteIndexes.stream().map(index -> dto.getImages().get(index).getId()).toList());

        logSrv.create(dto.getEntityType(), dto.getEntityId(), ChangelogField.IMAGE, ChangelogOperate.DELETE);
    }

    public ImageDisplayVO preview(ImagePreviewDTO dto) {
        IPage<Image> pages = page(new Page<>(1, dto.getCount()),
                new LambdaQueryWrapper<Image>().eq(Image::getEntityType, dto.getEntityType())
                        .eq(Image::getEntityId, dto.getEntityId())
                        .in(Image::getType, defaultImageType)
                        .orderByAsc(Image::getIdx)
        );
        return new ImageDisplayVO(converter.convert(pages.getRecords(), ImageVO.class), pages.getTotal());
    }

    public String getCache(int entityType, long entityId, ImageType imageType) {
        String key = STR."\{RedisKey.ENTITY_IMAGE_CACHE}\{imageType.getValue()}:\{entityType}:\{entityId}";
        String url = redisUtil.get(key, String.class);
        if(StringUtil.isEmpty(url)) {
            resetCache(entityType,  entityId);
            url = redisUtil.get(key, String.class);
        }
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
