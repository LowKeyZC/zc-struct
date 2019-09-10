package com.example.struct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

//启动类
@SpringBootApplication
//定时任务
@EnableScheduling
public class StructApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(StructApplication.class, args);
		System.out.println("ID:" + applicationContext.getId());
		System.out.println("application name:" + applicationContext.getApplicationName());
		System.out.println("display name:" + applicationContext.getDisplayName());
		System.out.println("start up date:" + new Date(applicationContext.getStartupDate()));
	}

}
