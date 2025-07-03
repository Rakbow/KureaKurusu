package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.ImageType;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
public class CommonImageUtil {

    private static final int DEFAULT_ENTRY_COVER_SIZE = 180;
    private static final int DEFAULT_ITEM_COVER_SIZE = 185;
    private static final int THUMB_SIZE_64 = 64;
    private static final int THUMB_SIZE_35 = 35;

    //region ------检测------

    //endregion

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

    @SneakyThrows
    public static ImageMiniDTO generateThumbTmp(ImageMiniDTO cover) {
        ImageMiniDTO thumb = new ImageMiniDTO();
        thumb.setName("Thumb");
        thumb.setType(ImageType.THUMB.getValue());

        MultipartFile originalFile = cover.getFile();

        // 读取原图
        BufferedImage original = ImageIO.read(originalFile.getInputStream());
        int width = original.getWidth();
        int height = original.getHeight();

        // 找最短边
        int side = Math.min(width, height);

        // 计算起点（中心为基准）
        int x = (width - side) / 2;
        int y = (height - side) / 2;

        // 裁剪为正方形
        BufferedImage cropped = original.getSubimage(x, y, side, side);
        // 生成缩略图到内存
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(cropped)
                .size(70, 70)
                .outputFormat("jpg")
                .toOutputStream(os);

        byte[] thumbBytes = os.toByteArray();
        // 创建 MultipartFile 对象（MockMultipartFile 实现类）
        MultipartFile thumbFile = new MockMultipartFile(
                "file",// 字段名
                "Thumb.jpg",// 文件名
                "image/jpeg",// MIME 类型
                thumbBytes// 内容
        );

        // 设置生成的 MultipartFile 到 DTO
        thumb.setFile(thumbFile);
        return thumb;
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
