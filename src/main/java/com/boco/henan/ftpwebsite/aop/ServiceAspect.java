package com.boco.henan.ftpwebsite.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 系统服务组件Aspect切面Bean
 */
// 声明这是一个组件
@Component
// 声明这是一个切面Bean
@Aspect
public class ServiceAspect {


    private final static Logger logger = LoggerFactory.getLogger(ServiceAspect.class);
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    // 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
    @Pointcut("execution(* com.boco.henan.*.controller..*(..))")
    public void aspect() {
    }

    /*
     * 配置前置通知,使用在方法aspect()上注册的切入点 同时接受JoinPoint切入点对象,可以没有该参数
     */
    @Before("aspect()")
    public void before(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("before " + joinPoint);
        }
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        startTime.set(System.currentTimeMillis());
    }

    // 配置后置通知,使用在方法aspect()上注册的切入点
    @AfterReturning(returning = "ret", pointcut = "aspect()")
    public void doAfterReturning(Object ret) throws Throwable {

        logger.info("RESPONSE : " + JSON.toJSONString(ret));
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));

        // Map<String, String> attachments = RpcContext.getContext().getAttachments();

    }
}
