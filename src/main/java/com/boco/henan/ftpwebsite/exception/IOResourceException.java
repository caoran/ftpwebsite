package com.boco.henan.ftpwebsite.exception;


public class IOResourceException extends GeneralException
{
    public IOResourceException(String errorCode)
    {
        super(errorCode);
    }

    public IOResourceException(String errorCode, String message) {
        super(errorCode, message);
    }

    public IOResourceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public IOResourceException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}