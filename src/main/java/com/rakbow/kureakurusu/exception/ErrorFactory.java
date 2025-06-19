package com.rakbow.kureakurusu.exception;

import com.qiniu.common.QiniuException;

/**
 * @author Rakbow
 * @since 2025/6/19 12:48
 */
public class ErrorFactory {

    public static ApiException entityNull() {
        return new ApiException("entity.url.error");
    }

    public static ApiException itemNull() {
        return new ApiException("item.url.error");
    }

    public static ApiException entryNull() {
        return new ApiException("entry.url.error");
    }

    public static ApiException fileEmpty() {
        return new ApiException("file.empty");
    }

    public static ApiException qiniuError(QiniuException e) {
        return new ApiException("qiniu.exception", e.response.toString());
    }

}
