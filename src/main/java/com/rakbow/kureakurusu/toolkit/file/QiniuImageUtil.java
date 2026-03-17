package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.vo.resource.UploadImage;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.dto.ImageDTO;
import com.rakbow.kureakurusu.data.enums.FileType;
import com.rakbow.kureakurusu.data.enums.ImageType;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.toolkit.FileUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-01 21:25
 */
@Component
@RequiredArgsConstructor
public class QiniuImageUtil {

    private final QiniuClient client;
    private final static String THUMBNAIL_URL = "?imageMogr2/auto-orient/thumbnail/";
    private final static String IMAGE_PREFIX = "upload/image/";

    /**
     * 通用新增图片
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @SneakyThrows
    public List<Image> batchUpload(int entityType, long entityId, List<ImageDTO.ImageMiniDTO> addImages) {
        List<Image> images = new ArrayList<>();
        for (ImageDTO.ImageMiniDTO miniImage : addImages) {
            //handle image
            UploadImage img = handleImage(entityType, entityId, miniImage);
            //upload image
            ActionResult ar = client.upload(img.getData(), img.getExtension(), img.getPrefixKey());
            if (!ar.state) throw new Exception(ar.message);
            Image image = new Image();
            image.setUrl(ar.data.toString().replace(Constant.FILE_DOMAIN, ""));
            image.setSize(img.getSize());
            image.setEntityType(entityType);
            image.setEntityId(entityId);
            image.setType(ImageType.get(miniImage.getType()));
            image.setName(miniImage.getName());
            image.setDetail(miniImage.getDetail());
            images.add(image);
        }
        return images;
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Integer> batchDelete(String[] keys) {
        return client.delete(keys);
    }

    public void deleteEntryImage(String key) {
        client.delete(new String[]{key});
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

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @SneakyThrows
    public String uploadEntryImage(int entityType, long entityId, ImageDTO.ImageMiniDTO image) {
        //handle image
        UploadImage img = handleImage(entityType, entityId, image);
        //upload image
        ActionResult ar = client.upload(img.getData(), img.getExtension(), img.getPrefixKey());
        if (!ar.state) throw new Exception(ar.message);
        return ar.data.toString();
    }

    @SneakyThrows
    private UploadImage handleImage(int entityType, long entityId, ImageDTO.ImageMiniDTO image) {
        UploadImage res = new UploadImage();
        //generate upload file pre-fix
        res.setPrefixKey(STR."\{IMAGE_PREFIX}\{entityType}/\{entityId}/");
        res.setData(image.getFile().getBytes());
        res.setSize(image.getFile().getSize());

        // 提取文件后缀（例如 "jpeg"）
        String originalFilename = image.getFile().getOriginalFilename();
        assert originalFilename != null;
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        if (StringUtil.equals(extension, "jpeg")) extension = "jpg";
        // 检测格式是否支持
        if (FileUtil.isFileFormatAllowed(extension, FileType.IMAGE)) {
            throw new ApiException("file.format.unsupported", FileType.IMAGE.getNameZh());
        }
        res.setExtension(extension);

        return res;
    }

}
