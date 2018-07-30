package com.boco.henan.ftpwebsite.exception;

public class RemoteServiceException extends ServiceException
{
    public RemoteServiceException(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public RemoteServiceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public RemoteServiceException(String errorCode) {
        super(errorCode);
    }

    public RemoteServiceException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
