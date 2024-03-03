package com.rakbow.kureakurusu.data.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/1/5 15:39
 */
@Data
public class File {

    private String url; //文件完整url
    private String name; //文件名
    private long size; //文件大小 单位 byte
    private String type; //类型
    private String uploadTime; //上传时间
    private String uploadUser; //上传用户

    public File() {
        url = "";
        name = "";
        type = "";
        size = 0;
        uploadTime = "";
        uploadUser = "";
    }

    @JsonIgnore
    public boolean isText() {
        return this.type.contains("text");
    }

    @JsonIgnore
    public boolean isAudio() {
        return this.type.contains("audio");
    }

}
