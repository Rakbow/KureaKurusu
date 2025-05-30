package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.FileType;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.toolkit.FileUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-01 21:25
 */
@Component
@RequiredArgsConstructor
public class QiniuImageUtil {

    private final QiniuBaseUtil qiniuBaseUtil;
    private final static String THUMBNAIL_URL = "?imageMogr2/auto-orient/thumbnail/";
    @Value("${kureakurusu.qiniu.image.domain}")
    private static String FILE_DOMAIN;

    /**
     * 通用新增图片
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ActionResult commonAddImages(int entityType, long entityId, MultipartFile[] files, List<Image> addImages) {
        ActionResult res = new ActionResult();
        try{
            //创建存储链接前缀
            String filePath = STR."upload/\{entityType}/\{entityId}/\{entityType}_\{entityId}_";
            for (int i = 0; i < files.length; i++) {
                //上传图片
                ActionResult ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.IMAGE);
                if (!ar.state) throw new Exception(ar.message);
                Image image = addImages.get(i);
                image.setUrl(ar.data.toString());
            }
        }catch(Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }

    /**
     * 通用新增图片
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @SneakyThrows
    public List<Image> commonAddImages(int entityType, long entityId, List<ImageMiniDTO> addImages) {
        List<Image> images = new ArrayList<>();
        for (ImageMiniDTO miniImage : addImages) {
            //handle image
            uploadImage img = handleImage(entityType, entityId, miniImage);
            //upload image
            ActionResult ar = qiniuBaseUtil.uploadFileToQiniu(img.getData(), img.getExtension(),  img.getPrefixKey());
            if (!ar.state) throw new Exception(ar.message);
            Image image = new Image();
            image.setUrl(ar.data.toString());
            image.setEntityType(entityType);
            image.setEntityId(entityId);
            image.setType(miniImage.getType());
            image.setName(miniImage.getName());
            image.setDetail(miniImage.getDetail());
            images.add(image);
        }
        return images;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Image> deleteImage(List<Image> deleteImages) {

        //delete image from qiniu server
        List<String> deleteResult = new ArrayList<>();
        //若删除的文件只有一个，调用单张删除方法
        if (deleteImages.size() == 1) {
            ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu(deleteImages.getFirst().getUrl());
            if (ar.fail()) throw new Exception(ar.message);
            deleteResult.add(deleteImages.getFirst().getUrl());
        } else {
            String[] fullFileUrlList = deleteImages.stream().map(Image::getUrl).toArray(String[]::new);
            ActionResult ar = qiniuBaseUtil.deleteFilesFromQiniu(fullFileUrlList);
            deleteResult.addAll((List<String>) ar.data);
        }
        return deleteImages.stream().filter(i -> deleteResult.contains(i.getUrl())).toList();
    }

    /**
     * delete all images
     *
     * @param images delete images
     * @author Rakbow
     */
    public void deleteAllImage(List<Image> images) {
        String[] deleteImageKeyList = images.stream().map(Image::getUrl).toArray(String[]::new);
        qiniuBaseUtil.deleteFilesFromQiniu(deleteImageKeyList);
    }

    /**
     * 获取等比固定高宽的缩略图URL
     *
     * @param image,size 原始图url，缩略图宽高
     * @return thumbImageUrl
     */
    public static String getThumb(String image, int size) {
        return STR."\{image}\{THUMBNAIL_URL}\{size}x\{size}";
    }

    public static String getThumb(String image, int size1, int size2) {
        return STR."\{image}\{THUMBNAIL_URL}\{size1}x\{size2}";
    }

    public static String getNoHeightLimitThumbUrl(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x";
    }

    public static String getCustomThumbUrl(String imageUrl, int size, int lengthLabel) {
        if(lengthLabel == 0) {
            return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x";
        }else {
            return STR."\{imageUrl}\{THUMBNAIL_URL}x\{size}";
        }
    }

    public static String getThumbUrlWidth(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}200x\{size}";
    }

    public static String getThumbBackgroundUrl(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x\{size}/extent/\{size}x\{size}/background/IzJmMzY0Zg==";
    }

    public static String getBookThumbBackgroundUrl(String imageUrl, double width, double height) {

        return STR."\{imageUrl}\{THUMBNAIL_URL}\{width}x\{height}/extent/\{width}x\{height}/background/IzJmMzY0Zg==";
    }

    /**
     * 通过外链获取图片key
     *
     * @param fullImageUrl 原始图url
     * @return thumbImageUrl
     */
    public static String getImageKeyByFullUrl(String fullImageUrl) {
        return fullImageUrl.replace(FILE_DOMAIN, "");
    }

    public static String getThumb70Url(List<Image> images) {
        if (!images.isEmpty()) {
            for (Image image : images) {
                if(image.isMain())
                    return getThumbBackgroundUrl(image.getUrl(), 70);
            }
        }
        return getThumb(CommonConstant.EMPTY_IMAGE_URL, 70);
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteEntryImage(String key) {
        ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu(key);
        if (ar.fail()) throw new Exception(ar.message);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @SneakyThrows
    public String uploadEntryImage(int entityType, long entityId, ImageMiniDTO image) {
        //handle image
        uploadImage img = handleImage(entityType, entityId, image);
        //upload image
        ActionResult ar = qiniuBaseUtil.uploadFileToQiniu(img.getData(), img.getExtension(),  img.getPrefixKey());
        if (!ar.state) throw new Exception(ar.message);
        return ar.data.toString();
    }

    @SneakyThrows
    private uploadImage handleImage(int entityType, long entityId, ImageMiniDTO image) {
        uploadImage res = new uploadImage();
        //generate upload file pre-fix
        res.setPrefixKey(STR."upload/\{entityType}/\{entityId}/\{entityType}_\{entityId}_");

        // 提取文件类型（例如 "jpeg"）
        String[] parts = image.getBase64Code().split(";base64,");
        String dataType = parts[0];  // "data:image/jpeg"
        String base64code = parts[1];  // Base64编码部分

        // 提取文件后缀（例如 "jpeg"）
        String extension = dataType.split("/")[1];  // "jpeg"
        if(StringUtils.equals(extension, "jpeg")) extension = "jpg";
        // 检测格式是否支持
        if (FileUtil.isFileFormatAllowed(extension, FileType.IMAGE)) {
            throw new Exception(I18nHelper.getMessage("file.format.unsupported", FileType.IMAGE.getNameZh()));
        }
        res.setExtension(extension);

        // encode Base64
        byte[] fileData = Base64.getDecoder().decode(base64code);
        // 检测文件是否为空
        if (fileData.length == 0) throw new Exception(I18nHelper.getMessage("file.empty"));
        res.setData(fileData);

        return res;
    }

}

@Data
class uploadImage {
    private String prefixKey;
    private byte[] data;
    private String extension;
}
