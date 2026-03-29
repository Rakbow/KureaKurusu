package com.rakbow.kureakurusu.exception;

import com.qiniu.common.QiniuException;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Rakbow
 * @since 2025/6/19 12:48
 */
public class ErrorFactory {

    public static AuthenticationException unauthorized() {
        throw new AuthenticationCredentialsNotFoundException(I18nHelper.getMessage("auth.no_login"));
    }

    public static AccessDeniedException noPermission() {
        throw new AccessDeniedException(I18nHelper.getMessage("auth.no_permission"));
    }

    public static ApiException entityNotFound() {
        return new ApiException("entity.url.error");
    }

    public static ApiException fileEmpty() {
        return new ApiException("file.empty");
    }

    public static ApiException qiniuError(QiniuException e) {
        return new ApiException("qiniu.exception", e.response.toString());
    }

}
