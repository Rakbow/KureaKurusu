package com.rakbow.kureakurusu.toolkit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author Rakbow
 * @since 2022-08-02 0:38
 */
public class CommonUtil {

    public static String getSubName(String nameEn, String nameZh) {
        return (nameEn != null && !nameEn.isBlank() && nameZh != null && !nameZh.isBlank())
                ? STR."\{nameEn} / \{nameZh}"
                : (nameEn != null && !nameEn.isBlank() ? nameEn : (nameZh != null && !nameZh.isBlank() ? nameZh : ""));
    }

    public static String camelToUnderline(String s) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(s);
    }

    public static String generateUUID(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return length == 0 ? uuid : uuid.substring(0, length);
    }

    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getFileSize(long bytes) {
        double kb = bytes / 1024.0;
        if (kb < 100) {
            return String.format("%.2f KB", kb);
        } else {
            double mb = kb / 1024.0;
            return String.format("%.2f MB", mb);
        }
    }

}
