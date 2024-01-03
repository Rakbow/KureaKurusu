package com.rakbow.kureakurusu.util.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.image.ImageProperty;
import com.rakbow.kureakurusu.data.emun.image.ImageType;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import com.rakbow.kureakurusu.data.vo.ImageVO;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.SpringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
public class CommonImageUtil {

    //region ------检测------

    /**
     * 对新增图片信息合法性进行检测，图片类型
     *
     * @param newImages,originalImages 新增图片信息，专辑原图片集合
     * @return boolean
     * @author rakbow
     */
    public static void checkAddImages(List<Image> newImages, List<Image> originalImages) throws Exception {

        int coverCount = 0;

        if (!originalImages.isEmpty()) {

            for (Image originalImage : originalImages) {
                if (originalImage.getType() == ImageType.COVER.getId()) {
                    coverCount++;
                }
            }
        }
        for (Image newImage : newImages) {
            if (newImage.getType() == ImageType.COVER.getId()) {
                coverCount++;
            }
        }

        //检测图片类型为封面的个数是否大于1
        if (coverCount > 1) {
            throw new Exception(I18nHelper.getMessage("image.error.only_one_cover"));
        }
    }

    /**
     * 对更新图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param images 图片json数组
     * @return 报错信息
     * @author rakbow
     */
    public static String checkUpdateImages(JSONArray images) {
        //封面类型的图片个数
        int coverCount = 0;
        for (int i = 0; i < images.size(); i++) {
            if (images.getJSONObject(i).getIntValue("type") == ImageType.COVER.getId()) {
                coverCount++;
            }
        }
        if (coverCount > 1) {
            return I18nHelper.getMessage("image.error.only_one_cover");
        }

        return "";
    }

    //endregion

    /**
     * 使用 通过图片url获取字节大小，长宽
     * @param imgUrl 图片URL
     */
    public static ImageProperty getImageProperty(String imgUrl) throws IOException {
        ImageProperty img = new ImageProperty();

        File file = new File(imgUrl);

        // 图片对象
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));

        img.setSize(file.length());
        img.setWidth(bufferedImage.getWidth());
        img.setHeight(bufferedImage.getHeight());

        return img;
    }

    /**
     * 通过遍历通用图片信息json数组获取封面url
     *
     * @param imageString 图片信息
     * @return coverUrl
     * @author rakbow
     */
    public static String getCoverUrl (String imageString) {
        JSONArray imageJson = JSON.parseArray(imageString);
        List<Image> images = imageJson.toJavaList(Image.class);
        for (Image image : images) {
            if (image.getType() == ImageType.COVER.getId()) {
                return image.getUrl();
            }
        }
        return CommonConstant.EMPTY_IMAGE_URL;
    }

    /**
     * 将图片切分为封面、展示和其他图片
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @param coverSize 封面图尺寸
     * @return segmentImagesResult
     * @author rakbow
     */
    public static segmentImagesResult segmentImages (String imagesJson, int coverSize, Entity entity, boolean isWidth) {

        segmentImagesResult result = new segmentImagesResult();

        List<ImageVO> ImageVOs = JSON.parseArray(imagesJson, ImageVO.class);

        //添加缩略图
        if (!ImageVOs.isEmpty()) {
            for (ImageVO imageVO : ImageVOs) {
                imageVO.setThumbUrl(QiniuImageUtil.getThumbUrl(imageVO.getUrl(), 70));
                imageVO.setThumbUrl50(QiniuImageUtil.getThumbUrl(imageVO.getUrl(), 50));
            }
        }

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        if (isWidth) {
            cover.put("url", QiniuImageUtil.getThumbUrlWidth(CommonConstant.EMPTY_IMAGE_WIDTH_URL, coverSize));
        }else {
            cover.put("url", QiniuImageUtil.getThumbUrl(getDefaultImageUrl(entity), coverSize));
        }
        cover.put("name", "404");
        if (!ImageVOs.isEmpty()) {
            for (ImageVO imageVO : ImageVOs) {
                if (imageVO.getType() == ImageType.COVER.getId()) {

                    if (isWidth) {
                        cover.put("url", QiniuImageUtil.getThumbUrlWidth(imageVO.getUrl(), coverSize));
                    }else {
                        cover.put("url", QiniuImageUtil.getThumbUrl(imageVO.getUrl(), coverSize));
                    }
                    cover.put("name", imageVO.getNameEn());
                }
            }
        }

        //对展示图片进行封装
        JSONArray displayImages = new JSONArray();
        if (!ImageVOs.isEmpty()) {
            for (ImageVO imageVO : ImageVOs) {
                if (imageVO.getType() == ImageType.DISPLAY.getId()
                        || imageVO.getType() == ImageType.COVER.getId()) {
                    displayImages.add(imageVO);
                }
            }
        }

        //对其他图片进行封装
        JSONArray otherImages = new JSONArray();
        if (!ImageVOs.isEmpty()) {
            for (ImageVO imageVO : ImageVOs) {
                if (imageVO.getType() == ImageType.OTHER.getId()) {
                    otherImages.add(imageVO);
                }
            }
        }

        result.images = JSON.parseArray(JSON.toJSONString(ImageVOs));
        result.cover = cover;
        result.displayImages = displayImages;
        result.otherImages = otherImages;

        return result;

    }

    /**
     * 获取各尺寸封面图片
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @return JSONObject
     * @author rakbow
     */
    public static JSONObject generateCover(String imagesJson, Entity entity) {

        List<Image> images = JSON.parseArray(imagesJson).toJavaList(Image.class);

        String defaultImageUrl = getDefaultImageUrl(entity);

        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, 200));
        cover.put("thumbUrl", QiniuImageUtil.getThumbUrl(defaultImageUrl, 50));
        cover.put("thumbUrl70", QiniuImageUtil.getThumbUrl(defaultImageUrl, 70));
        cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, 50));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (Image image : images) {
                if (image.getType() == ImageType.COVER.getId()) {
                    cover.put("url", QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), 200));
                    cover.put("thumbUrl", QiniuImageUtil.getThumbUrl(image.getUrl(), 50));
                    cover.put("thumbUrl70", QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), 70));
                    cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(image.getUrl(), 50));
                    cover.put("name", image.getNameEn());
                }
            }
        }
        return cover;
    }

    /**
     * 获取各尺寸封面图片(标准书本)
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @return JSONObject
     * @author rakbow
     */
    public static JSONObject generateBookCover(String imagesJson, Entity entity) {

        JSONArray images = JSONArray.parseArray(imagesJson);

        String defaultImageUrl = getDefaultImageUrl(entity);

        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageUtil.getBookThumbBackgroundUrl(defaultImageUrl, 180, 254.558));
        cover.put("thumbUrl", QiniuImageUtil.getThumbUrl(defaultImageUrl, 50));
        cover.put("thumbUrl70", QiniuImageUtil.getThumbUrl(defaultImageUrl, 70));
        cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, 50));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageUtil.getBookThumbBackgroundUrl(image.getString("url"), 180, 254.558));
                    cover.put("thumbUrl", QiniuImageUtil.getThumbUrl(image.getString("url"), 50));
                    cover.put("thumbUrl70", QiniuImageUtil.getThumbBackgroundUrl(image.getString("url"), 70));
                    cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(image.getString("url"), 50));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }
        return cover;
    }

    /**
     * 获取封面图片缩略图
     *
     * @param imagesJson 数据库原图片合集的JSON字符串
     * @return JSONObject
     * @author rakbow
     */
    public static JSONObject generateThumbCover(String imagesJson, Entity entity, int size) {
        JSONObject cover = new JSONObject();
        JSONArray images = JSONArray.parseArray(imagesJson);
        String defaultImageUrl = getDefaultImageUrl(entity);
        cover.put("url", QiniuImageUtil.getThumbUrl(defaultImageUrl, size));
        cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(defaultImageUrl, size));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageUtil.getThumbUrl(image.getString("url"), size));
                    cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(image.getString("url"), size));
                    cover.put("name", image.getString("nameEn"));
                }
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
