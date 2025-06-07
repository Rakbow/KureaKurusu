package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.FileType;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.security.MessageDigest;

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

    @SneakyThrows
    public static File convertToTempFile(MultipartFile multipartFile) {
        File tempFile = File.createTempFile("upload_", STR."_\{multipartFile.getOriginalFilename()}");
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    public static String getNewFilename(String filename) {
        String ext = filename.substring(filename.lastIndexOf("."));
        return STR."\{CommonUtil.generateUUID(0)}\{ext}";
    }

    @SneakyThrows
    public static String getMd5(File file) {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }

        byte[] digest = md.digest();
        // 转换为16进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

