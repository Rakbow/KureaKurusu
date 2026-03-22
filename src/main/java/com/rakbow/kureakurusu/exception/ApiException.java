package com.rakbow.kureakurusu.exception;

import com.rakbow.kureakurusu.toolkit.I18nHelper;

/**
 * @author Rakbow
 * @since 2025/6/19 12:38
 */
public class ApiException extends RuntimeException {

    public  ApiException() {
        super(I18nHelper.getMessage("system.internal_server_error"));
    }

    public ApiException(String messageKey) {
        super(I18nHelper.getMessage(messageKey));
    }

    public ApiException(String messageKey, String... args) {
        super(I18nHelper.getMessage(messageKey, args));
    }

}
