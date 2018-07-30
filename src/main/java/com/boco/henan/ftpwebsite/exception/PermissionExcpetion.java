package com.boco.henan.ftpwebsite.exception;

public class PermissionExcpetion extends ServiceException
{
    public PermissionExcpetion(String errorCode, String message)
    {
        super(errorCode, message);
    }

    public PermissionExcpetion(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public PermissionExcpetion(String errorCode) {
        super(errorCode);
    }

    public PermissionExcpetion(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
