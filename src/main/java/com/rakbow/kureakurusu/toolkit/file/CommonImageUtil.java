package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.ImageConfigValue;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageProperty;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.image.TempImage;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import com.rakbow.kureakurusu.data.vo.TempImageVO;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.SpringUtil;
import io.github.linpeilie.Converter;
import lombok.SneakyThrows;
import org.apache.poi.ss.formula.functions.Count;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.rakbow.kureakurusu.data.CommonConstant.*;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
public class CommonImageUtil {

    private static final Converter converter = SpringUtil.getBean(Converter.class);

    private static final int DEFAULT_THUMB_SIZE = 200;
    private static final int THUMB_SIZE_70 = 70;
    private static final int THUMB_SIZE_50 = 50;

    private static final double STANDARD_BOOK_WIDTH = 180;
    private static final double STANDARD_BOOK_HEIGHT = 254.558;

    private static final Map<ItemType, ImageConfigValue> itemImageConfigMap = new EnumMap<>(ItemType.class) {{
        put(ItemType.ALBUM, new ImageConfigValue(185, false, DEFAULT_ALBUM_IMAGE_URL));
        put(ItemType.BOOK, new ImageConfigValue(180, false, DEFAULT_BOOK_IMAGE_URL));
    }};

    private static final Map<EntityType, ImageConfigValue> entryImageConfigMap = new EnumMap<>(EntityType.class) {{
        put(EntityType.PRODUCT, new ImageConfigValue(200, false, EMPTY_IMAGE_URL));
        put(EntityType.FRANCHISE, new ImageConfigValue(100, true, EMPTY_IMAGE_URL));
    }};

    //region ------检测------

    /**
     * 对更新图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param images 图片json数组
     * @author rakbow
     */
    @SneakyThrows
    public static void checkUpdateImages(List<Image> images) {
        //封面类型的图片个数
        long coverCount = 0;
        coverCount += images.stream().filter(Image::isMain).count();
        for (Image image : images)
            if (image.isMain()) coverCount++;
        if (coverCount > 1)
            throw new Exception(I18nHelper.getMessage("image.error.only_one_cover"));
    }

    //endregion

    /**
     * 使用 通过图片url获取字节大小，长宽
     *
     * @param url 图片URL
     */
    @SneakyThrows
    public static ImageProperty getImageProperty(String url) {
        File file = new File(url);
        // 图片对象
        BufferedImage image = ImageIO.read(new FileInputStream(file));
        return ImageProperty.builder()
                .size(file.length())
                .height(image.getHeight())
                .width(image.getWidth())
                .build();
    }

    /**
     * 通过遍历通用图片信息json数组获取封面url
     *
     * @param images 图片
     * @return coverUrl
     * @author rakbow
     */
    public static String getCoverUrl(List<TempImage> images) {
        for (TempImage image : images) {
            if (image.isMain()) return image.getUrl();
        }
        return CommonConstant.EMPTY_IMAGE_URL;
    }

    public static String getThumbCoverUrl(List<TempImage> images) {
        for (TempImage image : images) {
            if (image.isMain()) return QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50);
        }
        return QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, THUMB_SIZE_50);
    }

    public static segmentImagesResult segmentItemImages(ItemType type, List<TempImage> images) {
        return segmentImages(images, itemImageConfigMap.get(type));
    }

    public static segmentImagesResult segmentEntryImages(EntityType type, List<TempImage> images) {
        return segmentImages(images, entryImageConfigMap.get(type));
    }

    /**
     * 将图片切分为封面、展示和其他图片
     *
     * @param orgImages,config 数据库原图片, 预设配置
     * @return segmentImagesResult
     * @author rakbow
     */
    private static segmentImagesResult segmentImages(List<TempImage> orgImages, ImageConfigValue config) {

        segmentImagesResult res = new segmentImagesResult();
        res.setImages(converter.convert(orgImages, TempImageVO.class));

        if (config.isWidth()) {
            res.setCoverUrl(QiniuImageUtil.getThumbUrlWidth(CommonConstant.EMPTY_IMAGE_WIDTH_URL, config.getCoverSize()));
        } else {
            res.setCoverUrl(QiniuImageUtil.getThumbUrl(config.getDefaultUrl(), config.getCoverSize()));
        }
        if (!res.getImages().isEmpty()) {
            for (TempImageVO image : res.getImages()) {
                //添加缩略图
                image.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
                image.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
                if (image.isMain()) {
                    //对封面图片进行处理
                    if (config.isWidth()) {
                        res.setCoverUrl(QiniuImageUtil.getThumbUrlWidth(image.getUrl(), config.getCoverSize()));
                    } else {
                        res.setCoverUrl(QiniuImageUtil.getThumbUrl(image.getUrl(), config.getCoverSize()));
                    }
                    res.getCover().setName(image.getNameEn());
                }
                if (image.isDisplay()) {
                    res.addDisplayImage(image);
                } else {
                    res.addOtherImage(image);
                }
            }
        }
        return res;
    }

    public static segmentImagesResult segmentImages(List<TempImage> orgImages) {

        segmentImagesResult res = new segmentImagesResult();
        res.setImages(converter.convert(orgImages, TempImageVO.class));
        if (!res.getImages().isEmpty()) {
            for (TempImageVO image : res.getImages()) {
                //添加缩略图
                image.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
                image.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
                if (image.isDisplay()) {
                    res.addDisplayImage(image);
                } else {
                    res.addOtherImage(image);
                }
            }
        }
        return res;
    }

//    /**
//     * 获取各尺寸封面图片
//     *
//     * @param images 数据库原图片
//     * @author rakbow
//     */
//    public static ImageVO generateCover(List<Image> images, Entity entity) {
//        //default image url
//        String defaultImageUrl = getDefaultImageUrl(entity);
//        //对图片封面进行处理
//        ImageVO cover = new ImageVO();
//        cover.setUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, DEFAULT_THUMB_SIZE));
//        cover.setThumbUrl50(QiniuImageUtil.getThumbUrl(defaultImageUrl, THUMB_SIZE_50));
//        cover.setThumbUrl70(QiniuImageUtil.getThumbUrl(defaultImageUrl, THUMB_SIZE_70));
//        cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, THUMB_SIZE_50));
//        if (!images.isEmpty()) {
//            for (Image image : images) {
//                if (!image.isMain()) continue;
//                cover.setUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), DEFAULT_THUMB_SIZE));
//                cover.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
//                cover.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
//                cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), THUMB_SIZE_50));
//                cover.setName(image.getNameEn());
//            }
//        }
//        return cover;
//    }

//    /**
//     * 获取封面图片缩略图
//     *
//     * @param orgImages 数据库原图片合集
//     * @return JSONObject
//     * @author rakbow
//     */
//    public static ImageVO generateThumbCover(List<Image> orgImages, Entity entity, int size) {
//        String defaultImageUrl = getDefaultImageUrl(entity);
//        ImageVO cover = new ImageVO();
//        List<ImageVO> images = VOMapper.toVO(orgImages);
//        cover.setUrl(QiniuImageUtil.getThumbUrl(defaultImageUrl, size));
//        cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, size));
//        if (!images.isEmpty()) {
//            for (Image image : images) {
//                if (!image.isMain()) continue;
//                cover.setUrl(QiniuImageUtil.getThumbUrl(image.getUrl(), size));
//                cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), size));
//                cover.setName(image.getNameEn());
//            }
//        }
//        return cover;
//    }

}
