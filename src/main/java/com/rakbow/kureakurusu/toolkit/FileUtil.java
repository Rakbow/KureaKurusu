package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.FileType;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Rakbow
 * @since 2022-11-30 22:14
 */
public class FileUtil {

    // 允许的后缀扩展名
    public static String[] IMAGE_FILE_FORMATS = new String[]{"png", "bmp", "jpg", "jpeg", "webp", "gif"};
    public static String[] AUDIO_FILE_FORMATS = new String[]{"mp3"};
    public static String[] TEXT_FILE_FORMATS = new String[]{"lrc"};

    public static boolean isFileFormatAllowed(String fileFormat, FileType fileType) {
        String[] formats = switch (fileType) {
            case FileType.IMAGE -> IMAGE_FILE_FORMATS;
            case FileType.AUDIO -> AUDIO_FILE_FORMATS;
            case FileType.TEXT -> TEXT_FILE_FORMATS;
            default -> new String[]{};
        };
        for (String format : formats)
            if (format.equals(fileFormat)) return false;
        return true;
    }

    //删除服务器上的文件
    //dir: 文件夹路径，fileName: 文件名（不包含后缀）
    public static void deleteFile(Path dir, String fileName){
        File[] files = new File(dir.toUri()).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().substring(0, file.getName().lastIndexOf(".")).equals(fileName)) {
                file.delete();
            }
        }
    }
}

