package com.boco.henan.ftpwebsite.listener;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ftpServerListener implements ApplicationListener<ContextRefreshedEvent>{

    private Logger logger = LoggerFactory.getLogger(ftpServerListener.class);

    private boolean ftpStarted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        /*if (!this.ftpStarted)
        {
            this.logger.info("准备启动文件专用ftp服务器，端口[221]...");
            FtpServerFactory serverFactory = new FtpServerFactory();
            ListenerFactory factory = new ListenerFactory();

            factory.setPort(221);

            serverFactory.addListener("default", factory.createListener());

            PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
            userManagerFactory.setFile(new File("ftp-cfg.properties"));
            userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
            serverFactory.setUserManager(userManagerFactory.createUserManager());

            FtpServer server = serverFactory.createServer();
            try {
                server.start();
                this.logger.info("文件ftp服务器启动成功，端口[221]");
            } catch (FtpException e) {
                throw new RuntimeException(e);
            }
            this.ftpStarted = true;
        }*/
    }
}
