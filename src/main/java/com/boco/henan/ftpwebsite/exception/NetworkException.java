package com.boco.henan.ftpwebsite.exception;

public class NetworkException extends IOResourceException
{
    public NetworkException(String errorCode)
    {
        super(errorCode);
    }

    public NetworkException(String errorCode, String message) {
        super(errorCode, message);
    }

    public NetworkException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public NetworkException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
