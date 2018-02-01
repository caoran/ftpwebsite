package com.boco.henan.ftpwebsite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.boco.henan.ftpwebsite.dao")
public class FtpwebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtpwebsiteApplication.class, args);
	}
}
