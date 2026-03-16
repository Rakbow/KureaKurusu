package com.rakbow.kureakurusu.exception;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String messageKey) {
        super(messageKey);
    }

}
