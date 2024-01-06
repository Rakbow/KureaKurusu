package com.rakbow.kureakurusu.util.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.image.ImageProperty;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import com.rakbow.kureakurusu.data.vo.ImageVO;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.GeneralVOMapper;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
public class CommonImageUtil {

    private static final GeneralVOMapper VOMapper = GeneralVOMapper.INSTANCES;

    private static final int DEFAULT_THUMB_SIZE = 200;
    private static final int THUMB_SIZE_70 = 70;
    private static final int THUMB_SIZE_50 = 50;

    private static final double STANDARD_BOOK_WIDTH = 180;
    private static final double STANDARD_BOOK_HEIGHT = 254.558;

    //region ------检测------

    /**
     * 对新增图片信息合法性进行检测，图片类型
     *
     * @param newImages,originalImages 新增图片信息，专辑原图片集合
     * @author rakbow
     */
    @SneakyThrows
    public static void checkAddImages(List<Image> newImages, List<Image> originalImages) {
        int coverCount = 0;
        if (!originalImages.isEmpty()) {
            for (Image originalImage : originalImages) {
                if (originalImage.isMain()) coverCount++;
            }
        }
        for (Image newImage : newImages) {
            if (newImage.isMain()) coverCount++;
        }
        //检测图片类型为封面的个数是否大于1
        if (coverCount > 1)
            throw new Exception(I18nHelper.getMessage("image.error.only_one_cover"));
    }

    /**
     * 对更新图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param images 图片json数组
     * @author rakbow
     */
    @SneakyThrows
    public static void checkUpdateImages(List<Image> images) {
        //封面类型的图片个数
        int coverCount = 0;
        for (Image image : images) {
            if (image.isMain()) coverCount++;
        }
        if (coverCount > 1)
            throw new Exception(I18nHelper.getMessage("image.error.only_one_cover"));
    }

    //endregion

    /**
     * 使用 通过图片url获取字节大小，长宽
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
    public static String getCoverUrl (List<Image> images) {
        for (Image image : images) {
            if (image.isMain()) return image.getUrl();
        }
        return CommonConstant.EMPTY_IMAGE_URL;
    }

    /**
     * 将图片切分为封面、展示和其他图片
     *
     * @param orgImages 数据库原图片
     * @param coverSize 封面图尺寸
     * @return segmentImagesResult
     * @author rakbow
     */
    public static segmentImagesResult segmentImages (List<Image> orgImages, int coverSize, Entity entity, boolean isWidth) {

        segmentImagesResult res = new segmentImagesResult();
        res.setImages(VOMapper.toVO(orgImages));

        if (isWidth) {
            res.setCoverUrl(QiniuImageUtil.getThumbUrlWidth(CommonConstant.EMPTY_IMAGE_WIDTH_URL, coverSize));
        }else {
            res.setCoverUrl(QiniuImageUtil.getThumbUrl(getDefaultImageUrl(entity), coverSize));
        }
        if (!res.getImages().isEmpty()) {
            for (ImageVO image : res.getImages()) {
                //添加缩略图
                image.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
                image.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
                if (image.isMain()) {
                    //对封面图片进行处理
                    if (isWidth) {
                        res.setCoverUrl(QiniuImageUtil.getThumbUrlWidth(image.getUrl(), coverSize));
                    }else {
                        res.setCoverUrl(QiniuImageUtil.getThumbUrl(image.getUrl(), coverSize));
                    }
                    res.getCover().setName(image.getNameEn());
                }
                if (image.isDisplay()) {
                    res.addDisplayImage(image);
                }else {
                    res.addOtherImage(image);
                }
            }
        }
        return res;
    }

    /**
     * 获取各尺寸封面图片
     *
     * @param images 数据库原图片
     * @author rakbow
     */
    public static ImageVO generateCover(List<Image> images, Entity entity) {
        //default image url
        String defaultImageUrl = getDefaultImageUrl(entity);
        //对图片封面进行处理
        ImageVO cover = new ImageVO();
        cover.setUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, DEFAULT_THUMB_SIZE));
        cover.setThumbUrl50(QiniuImageUtil.getThumbUrl(defaultImageUrl, THUMB_SIZE_50));
        cover.setThumbUrl70(QiniuImageUtil.getThumbUrl(defaultImageUrl, THUMB_SIZE_70));
        cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, THUMB_SIZE_50));
        if (!images.isEmpty()) {
            for (Image image : images) {
                if(!image.isMain()) continue;
                cover.setUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), DEFAULT_THUMB_SIZE));
                cover.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
                cover.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
                cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), THUMB_SIZE_50));
                cover.setName(image.getNameEn());
            }
        }
        return cover;
    }

    /**
     * 获取各尺寸封面图片(标准书本)
     *
     * @param orgImages 数据库原图片
     * @return JSONObject
     * @author rakbow
     */
    public static ImageVO generateBookCover(List<Image> orgImages, Entity entity) {
        String defaultImageUrl = getDefaultImageUrl(entity);
        List<ImageVO> images = VOMapper.toVO(orgImages);

        //对图片封面进行处理
        ImageVO cover = new ImageVO();
        cover.setUrl(QiniuImageUtil.getBookThumbBackgroundUrl(defaultImageUrl, STANDARD_BOOK_WIDTH, STANDARD_BOOK_HEIGHT));
        cover.setThumbUrl50(QiniuImageUtil.getThumbUrl(defaultImageUrl, THUMB_SIZE_50));
        cover.setThumbUrl70(QiniuImageUtil.getThumbUrl(defaultImageUrl, THUMB_SIZE_70));
        cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, THUMB_SIZE_50));
        if (!images.isEmpty()) {
            for (Image image : images) {
                if(!image.isMain()) continue;
                cover.setUrl(QiniuImageUtil.getBookThumbBackgroundUrl(image.getUrl(), STANDARD_BOOK_WIDTH, STANDARD_BOOK_HEIGHT));
                cover.setThumbUrl50(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_50));
                cover.setThumbUrl70(QiniuImageUtil.getThumbUrl(image.getUrl(), THUMB_SIZE_70));
                cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), THUMB_SIZE_50));
                cover.setName(image.getNameEn());
            }
        }
        return cover;
    }

    /**
     * 获取封面图片缩略图
     *
     * @param orgImages 数据库原图片合集
     * @return JSONObject
     * @author rakbow
     */
    public static ImageVO generateThumbCover(List<Image> orgImages, Entity entity, int size) {
        String defaultImageUrl = getDefaultImageUrl(entity);
        ImageVO cover = new ImageVO();
        List<ImageVO> images = VOMapper.toVO(orgImages);
        cover.setUrl(QiniuImageUtil.getThumbUrl(defaultImageUrl, size));
        cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, size));
        if (!images.isEmpty()) {
            for (Image image : images) {
                if(!image.isMain()) continue;
                cover.setUrl(QiniuImageUtil.getThumbUrl(image.getUrl(), size));
                cover.setBlackUrl(QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), size));
                cover.setName(image.getNameEn());
            }
        }
        return cover;
    }

    public static String getDefaultImageUrl(Entity entity) {
        switch (entity) {
            case ALBUM -> {
                return CommonConstant.DEFAULT_ALBUM_IMAGE_URL;
            }
            case BOOK -> {
                return CommonConstant.DEFAULT_BOOK_IMAGE_URL;
            }
            case DISC -> {
                return CommonConstant.DEFAULT_DISC_IMAGE_URL;
            }
            case GAME -> {
                return CommonConstant.DEFAULT_GAME_IMAGE_URL;
            }
//            case MERCH -> {
//                return CommonConstant.DEFAULT_MERCH_IMAGE_URL;
//            }
//            case FRANCHISE -> {
//                return CommonConstant.DEFAULT_FRANCHISE_IMAGE_URL;
//            }
//            case PRODUCT -> {
//                return CommonConstant.DEFAULT_PRODUCT_IMAGE_URL;
//            }
            default -> {
                return CommonConstant.EMPTY_IMAGE_URL;
            }
        }
    }

}
