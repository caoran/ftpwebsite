package com.boco.henan.ftpwebsite.exception;

public class GeneralException extends Exception{

    private String errorCode;

    public GeneralException(String errorCode)
    {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public GeneralException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GeneralException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public GeneralException(String errorCode, Throwable cause) {
        this(errorCode);
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
