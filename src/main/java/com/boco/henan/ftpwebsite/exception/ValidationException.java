package com.boco.henan.ftpwebsite.exception;

public class ValidationException extends GeneralException {
    public ValidationException(String errorCode) {
        super(errorCode);
    }

    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ValidationException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
