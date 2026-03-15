package com.rakbow.kureakurusu.exception;

import com.rakbow.kureakurusu.toolkit.I18nHelper;

public class PermissionException extends RuntimeException {

    public PermissionException(String messageKey) {
        super(I18nHelper.getMessage(messageKey));
    }

    public PermissionException(String messageKey, String... args) {
        super(I18nHelper.getMessage(messageKey, args));
    }
}
