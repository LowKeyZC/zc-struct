package com.example.struct;

import com.example.struct.domain.User;
import com.example.struct.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StructApplicationTests {

	@Resource
	private UserService userService;

	@Test
	public void contextLoads() {
		User user = userService.selectById("111");
	}

}
