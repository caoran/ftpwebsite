package com.boco.henan.ftpwebsite.exception.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value={"exception"}, name="exception", factory=GeneralExceptionPropertySourceFactory.class)
public class GeneralExceptionAutoConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionAutoConfig.class);

    public GeneralExceptionAutoConfig() {
        LOGGER.info("统一异常处理模块配置初始化...");
    }
}
