package com.rakbow.kureakurusu.util.common;

import com.rakbow.kureakurusu.data.emun.system.FileType;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-30 22:14
 * @Description:
 */
public class FileUtil {

    // 允许的后缀扩展名
    public static String[] IMAGE_FILE_FORMATS = new String[] { "png", "bmp", "jpg", "jpeg"};
    public static String[] AUDIO_FILE_FORMATS = new String[] { "mp3"};
    public static String[] TEXT_FILE_FORMATS = new String[] { "lrc"};

    public static boolean isFileFormatAllowed(String fileFormat, FileType fileType) {
        String[] formats = null;
        if (fileType == FileType.IMAGE) {
            formats = IMAGE_FILE_FORMATS;
        }
        if (fileType == FileType.AUDIO) {
            formats = AUDIO_FILE_FORMATS;
        }
        if (fileType == FileType.TEXT) {
            formats = TEXT_FILE_FORMATS;
        }
        for (String format : formats) {
            if (format.equals(fileFormat)) {
                return true;
            }
        }
        return false;
    }
}

