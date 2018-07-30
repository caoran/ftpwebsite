package com.boco.henan.ftpwebsite.exception;


public class ServiceException extends GeneralException
{
    public ServiceException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public ServiceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ServiceException(String errorCode) {
        super(errorCode);
    }

    public ServiceException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
