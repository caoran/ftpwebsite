package com.boco.henan.ftpwebsite.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Environment environment;


    //添加全局异常处理流程，根据需要设置需要处理的异常，本文以MethodArgumentNotValidException为例
    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidHandler(HttpServletRequest request,
                                                MethodArgumentNotValidException exception) throws Exception
    {
        LOGGER.error("============MethodArgumentNotValidHandler==================");
        LOGGER.error(exception.getMessage(),exception);
        //按需重新封装需要返回的错误信息
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>();
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(error.getDefaultMessage());
            invalidArgument.setField(error.getField());
            invalidArgument.setRejectedValue(error.getRejectedValue());
            invalidArguments.add(invalidArgument);
        }

        ErrorResult errorResult = new ErrorResult("",invalidArguments.toString(),request.getRequestURI(),"");
        return errorResult;
    }

    @ExceptionHandler(BindException.class)
    public Object handleBindException(HttpServletRequest request,BindException e) {
        LOGGER.error("==============handleBindException=================================");
        LOGGER.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        ErrorResult errorResult = new ErrorResult("",message,request.getRequestURI(),"");
        return errorResult;
    }

    @ExceptionHandler(value = GeneralException.class)
    public ErrorResult errorHandlerOverJson(HttpServletRequest request,
                                           GeneralException exception) {
        LOGGER.error("============errorHandlerOverJson==================");
        LOGGER.error(exception.getMessage(),exception);
        String errorMessage=this.environment.getProperty(exception.getErrorCode());
        ErrorResult errorResult = new ErrorResult(exception.getErrorCode(), errorMessage, request.getRequestURI(), new ServletException(errorMessage));
        return errorResult;
    }


    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        LOGGER.error("---DefaultException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getMessage());
        LOGGER.error("",e);
        e.printStackTrace();
        return e.getMessage();
    }
}
