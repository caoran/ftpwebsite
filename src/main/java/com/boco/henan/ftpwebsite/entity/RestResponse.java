package com.boco.henan.ftpwebsite.entity;

public class RestResponse<T> {
    private boolean success = true;
    private String message;
    private T data;

    public RestResponse() {
    }

    public RestResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RestResponse(boolean success, String message, T data) {
        this(success,message);
        this.data=data;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

}
