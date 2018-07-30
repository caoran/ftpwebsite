package com.boco.henan.ftpwebsite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FtpwebsiteApplicationTests {

    private static final Logger LOGGER= LoggerFactory.getLogger(FtpwebsiteApplicationTests.class);

	@Autowired
	private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

	@Test
	public void sendSimpleMail() throws Exception {
        LOGGER.info("开始发送邮件测试。。。，发送账号：{}",Sender);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(Sender);
		message.setTo(Sender);
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容");

		mailSender.send(message);
	}


	@Test
	public void contextLoads() {
	}

}
