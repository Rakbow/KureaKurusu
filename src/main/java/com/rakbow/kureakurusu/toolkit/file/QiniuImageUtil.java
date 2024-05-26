package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.FileType;
import com.rakbow.kureakurusu.data.image.Image;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
            String entityName = EntityType.getTableName(entityType);
            //创建存储链接前缀
            String filePath = STR."\{entityName}/\{entityId}/";
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
     * @param imageUrl,size 原始图url，缩略图宽高
     * @return thumbImageUrl
     */
    public static String getThumbUrl(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x\{size}";
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
        return getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 70);
    }

}
