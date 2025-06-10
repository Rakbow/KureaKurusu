package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.ImageConfigValue;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.ImageProperty;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
public class CommonImageUtil {

    private static final int DEFAULT_ENTRY_COVER_SIZE = 180;
    private static final int DEFAULT_ITEM_COVER_SIZE = 185;
    private static final int THUMB_SIZE_64 = 64;
    private static final int THUMB_SIZE_70 = 70;
    private static final int THUMB_SIZE_50 = 50;
    private static final int THUMB_SIZE_35 = 35;

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

    public static String getItemImage(int imageType, String cover) {
        if (imageType == ImageType.MAIN.getValue()) {
            return QiniuImageUtil.getThumb(cover, DEFAULT_ITEM_COVER_SIZE);
        }else {
            return QiniuImageUtil.getThumb(cover, THUMB_SIZE_64);
        }
    }

    //TODO
    public static String getEntryCover(String orgUrl) {
        String url = StringUtils.isBlank(orgUrl) ? CommonConstant.EMPTY_IMAGE_URL : STR."\{Constant.FILE_DOMAIN}\{orgUrl}";
        return QiniuImageUtil.getNoHeightLimitThumbUrl(url, DEFAULT_ENTRY_COVER_SIZE);
    }

    public static String getEntryThumb(String orgUrl) {
        String url = StringUtils.isBlank(orgUrl) ? CommonConstant.EMPTY_IMAGE_URL : STR."\{Constant.FILE_DOMAIN}\{orgUrl}";
        return QiniuImageUtil.getThumb(url, THUMB_SIZE_35);
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
            image.setThumb70(QiniuImageUtil.getThumb(image.getUrl(), THUMB_SIZE_70));
            image.setThumb50(QiniuImageUtil.getThumb(image.getUrl(), THUMB_SIZE_50));
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
            image.setThumb70(QiniuImageUtil.getThumb(image.getUrl(), THUMB_SIZE_70));
            image.setThumb50(QiniuImageUtil.getThumb(image.getUrl(), THUMB_SIZE_50));
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
            i.setThumb70(QiniuImageUtil.getThumb(i.getUrl(), THUMB_SIZE_70));
            i.setThumb50(QiniuImageUtil.getThumb(i.getUrl(), THUMB_SIZE_50));
            i.setThumb(QiniuImageUtil.getThumb(i.getUrl(), 1200, 650));
        });
    }


    @SneakyThrows
    public static String generateThumb(String coverBase64Code) {
        // 提取文件类型（例如 "jpeg"）
        String[] parts = coverBase64Code.split(";base64,");
        String dataType = parts[0];  // "data:image/jpeg"
        String base64code = parts[1];  // Base64编码部分

        byte[] imageBytes = Base64.getDecoder().decode(base64code);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(imageBytes))
                .size(THUMB_SIZE_64, THUMB_SIZE_64)
                .outputFormat("jpg")
                .toOutputStream(os);
        return STR."\{dataType};base64,\{Base64.getEncoder().encodeToString(os.toByteArray())}";
    }

    @SneakyThrows
    public static String getBase64CodeByUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            String contentType = connection.getContentType(); // 如 image/jpeg

            try (InputStream in = connection.getInputStream()) {
                byte[] imageBytes = in.readAllBytes();
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                return STR."data:\{contentType};base64,\{base64}";
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get image from URL", e);
        }
    }

    @SneakyThrows
    public static ImageMiniDTO generateThumb(ImageMiniDTO cover) {
        ImageMiniDTO thumb = new ImageMiniDTO();
        thumb.setName("Thumb");
        thumb.setType(ImageType.THUMB.getValue());


        MultipartFile originalFile = cover.getFile();

        // 生成缩略图到内存
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(originalFile.getInputStream())
                .size(THUMB_SIZE_64, THUMB_SIZE_64)
                .outputFormat("jpg")
                .toOutputStream(os);

        byte[] thumbBytes = os.toByteArray();
        // 创建 MultipartFile 对象（MockMultipartFile 实现类）
        MultipartFile thumbFile = new MockMultipartFile(
                "file",                              // 字段名
                "Thumb.jpg",                         // 文件名
                "image/jpeg",                        // MIME 类型
                thumbBytes                           // 内容
        );

        // 设置生成的 MultipartFile 到 DTO
        thumb.setFile(thumbFile);
        return thumb;
    }

    @SneakyThrows
    public static MultipartFile base64ToMultipartFile(String base64) {
        String[] parts = base64.split(",");
        String base64Data = parts.length > 1 ? parts[1] : parts[0];

        // 提取 MIME 类型（可选）
        String contentType = "application/octet-stream";
        if (parts[0].contains("data:") && parts[0].contains(";")) {
            contentType = parts[0].substring(parts[0].indexOf(":") + 1, parts[0].indexOf(";"));
        }

        byte[] fileBytes = Base64.getDecoder().decode(base64Data);

        return new MockMultipartFile("file", "filename", contentType, fileBytes);
    }

}
