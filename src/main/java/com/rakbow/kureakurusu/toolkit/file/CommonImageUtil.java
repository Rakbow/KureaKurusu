package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.ImageConfigValue;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageProperty;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

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
        put(EntityType.PRODUCT, new ImageConfigValue(180, false, EMPTY_IMAGE_URL));
        put(EntityType.FRANCHISE, new ImageConfigValue(100, true, EMPTY_IMAGE_URL));
    }};

    //region ------检测------

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

    public static String getItemCover(ItemType type, Image image) {
        ImageConfigValue config = itemImageConfigMap.get(type);
        String url = image == null ? config.getDefaultUrl() : image.getUrl();
        return QiniuImageUtil.getThumbUrl(url, config.getCoverSize());
    }

    public static String getEntityCover(EntityType type, Image image) {
        ImageConfigValue config = entryImageConfigMap.get(type);
        String url = image == null ? config.getDefaultUrl() : image.getUrl();
        return QiniuImageUtil.getThumbUrl(url, config.getCoverSize());
    }

    public static String getThumbCover(Image image) {
        if (image != null) return QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), THUMB_SIZE_50);
        return QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, THUMB_SIZE_50);
    }

    public static String getEntryThumbCover(String orgUrl) {
        if(StringUtils.isBlank(orgUrl))
            return QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, THUMB_SIZE_50);
        int dotIndex = orgUrl.lastIndexOf('.');
        String newUrl = STR."\{Constant.FILE_DOMAIN}\{orgUrl.substring(0, dotIndex)}_1\{orgUrl.substring(dotIndex)}";
        return QiniuImageUtil.getThumbUrl(newUrl, THUMB_SIZE_50);
    }

    public static segmentImagesResult segmentItemImages(ItemType type, List<Image> images) {
        return segmentImages(images, itemImageConfigMap.get(type));
    }

    public static segmentImagesResult segmentEntryImages(EntityType type, List<Image> images) {
        return segmentImages(images, entryImageConfigMap.get(type));
    }

    /**
     * 将图片切分为封面、展示和其他图片
     *
     * @author rakbow
     */
    private static segmentImagesResult segmentImages(List<Image> images, ImageConfigValue config) {
        segmentImagesResult res = new segmentImagesResult();
        if (images.isEmpty()) return res;
        for (Image image : images) {
            //添加缩略图
            image.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
            image.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
            if (!image.isDisplay())
                res.addOtherImage(image);
            else
                res.addDisplayImage(image);
        }
        res.setImages(images);
        return res;
    }

    public static segmentImagesResult segmentImages(List<Image> images) {

        segmentImagesResult res = new segmentImagesResult();
        if (images.isEmpty()) return res;
        for (Image image : images) {
            //generate thumb image
            image.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
            image.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
            if (image.isDisplay())
                res.addDisplayImage(image);
            else
                res.addOtherImage(image);
        }
        res.setImages(images);
        return res;
    }

    public static void generateThumb(List<Image> images) {
        images.forEach(i -> {
            i.setThumbUrl70(QiniuImageUtil.getThumbUrl(i.getUrl(), THUMB_SIZE_70));
            i.setThumbUrl50(QiniuImageUtil.getThumbUrl(i.getUrl(), THUMB_SIZE_50));
        });
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
