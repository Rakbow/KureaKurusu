package com.rakbow.kureakurusu.exception;

import com.rakbow.kureakurusu.toolkit.I18nHelper;

public class PermissionException extends ApiException {

    public PermissionException(String messageKey) {
        super(messageKey);
    }

}
