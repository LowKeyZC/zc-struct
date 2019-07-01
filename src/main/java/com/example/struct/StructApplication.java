package com.example.struct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

//启动类
@SpringBootApplication
//定时任务
@EnableScheduling
public class StructApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(StructApplication.class, args);
	}

}
