package com.boco.henan.ftpwebsite.exception.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class GeneralExceptionPropertySource extends PropertySource<GeneralExceptionPropertySource> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionPropertySource.class);
    private static final String USERDEF_ERROR_RESOURCE_PATH = "/exception/errors.properties";
    private static final String INTERNAL_ERROR_RESOURCE_PATH = "/exception/internal-errors.properties";
    private static final String FILE_ENCODING = "utf-8";
    private Properties properties = new Properties();

    private PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    public GeneralExceptionPropertySource(String name) throws IOException{
        super(name);
        tryResource(USERDEF_ERROR_RESOURCE_PATH);
        tryResource(INTERNAL_ERROR_RESOURCE_PATH);
        Resource[] resources = this.resourceResolver.getResources("classpath:/exception/**/errors*.properties");
        for (Resource resource : resources)
            tryResourceSafely(resource.getInputStream());
    }


    private void tryResourceSafely(InputStream inputStream) throws IOException
    {
        try
        {
            if (inputStream != null)
                this.properties.load(new InputStreamReader(inputStream, "utf-8"));
        } catch (IOException e) {
            LOGGER.warn("加载错误码映射配置失败", e);
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

    private void tryResource(String file) throws IOException {
        tryResourceSafely(getClass().getResourceAsStream(file));
    }

    @Override
    public Object getProperty(String s) {
        return properties.get(s);
    }
}
