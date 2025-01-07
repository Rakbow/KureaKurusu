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
    private static final int DEFAULT_COVER_SIZE = 180;
    private static final int THUMB_SIZE_64 = 64;
    private static final int THUMB_SIZE_70 = 70;
    private static final int THUMB_SIZE_50 = 50;
    private static final int THUMB_SIZE_35 = 35;

    private static final double STANDARD_BOOK_WIDTH = 180;
    private static final double STANDARD_BOOK_HEIGHT = 254.558;

    private static final Map<ItemType, ImageConfigValue> itemImageConfigMap = new EnumMap<>(ItemType.class) {{
        put(ItemType.ALBUM, new ImageConfigValue(185, false, DEFAULT_ALBUM_IMAGE_URL));
        put(ItemType.BOOK, new ImageConfigValue(180, false, DEFAULT_BOOK_IMAGE_URL));
        put(ItemType.GOODS, new ImageConfigValue(185, false, EMPTY_IMAGE_URL));
        put(ItemType.FIGURE, new ImageConfigValue(185, false, EMPTY_IMAGE_URL));
    }};

    private static final Map<EntityType, ImageConfigValue> entryImageConfigMap = new EnumMap<>(EntityType.class) {{
        put(EntityType.PERSON, new ImageConfigValue(200, false, EMPTY_IMAGE_URL));
        put(EntityType.PRODUCT, new ImageConfigValue(200, false, EMPTY_IMAGE_URL));
        put(EntityType.CHARACTER, new ImageConfigValue(200, false, EMPTY_IMAGE_URL));
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

    public static String getItemCover(ItemType type, String cover) {
        ImageConfigValue config = itemImageConfigMap.get(type);
        return QiniuImageUtil.getThumbUrl(cover, config.getCoverSize());
    }

    public static String getItemThumb(Image image) {
        if (image != null) return image.getUrl();
        return QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, THUMB_SIZE_64);
    }

    //TODO
    public static String getEntryCover(String orgUrl) {
        String url = StringUtils.isBlank(orgUrl) ? CommonConstant.EMPTY_IMAGE_URL : STR."\{Constant.FILE_DOMAIN}\{orgUrl}";
        return QiniuImageUtil.getNoHeightLimitThumbUrl(url, DEFAULT_COVER_SIZE);
    }

    public static String getEntryThumb(String orgUrl) {
        String url = StringUtils.isBlank(orgUrl) ? CommonConstant.EMPTY_IMAGE_URL : STR."\{Constant.FILE_DOMAIN}\{orgUrl}";
        return QiniuImageUtil.getThumbUrl(url, THUMB_SIZE_35);
    }

    public static String getThumbCover(Image image) {
        if (image != null) return QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), THUMB_SIZE_50);
        return QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, THUMB_SIZE_50);
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

}
