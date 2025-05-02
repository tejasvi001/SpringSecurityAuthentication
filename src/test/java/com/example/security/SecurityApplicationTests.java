package com.example.security;

import com.example.security.entities.User;
import com.example.security.services.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {
	@Autowired
	private JWTService jwtService;

	@Test
	void contextLoads() {
		User user=new User(4L,"anuj@gmail.com","1234","anuj");
		String token=jwtService.generateToken(user);
		System.out.println(token);
		Long id= jwtService.getUserIdFromToken(token);
		System.out.println(id);

	}

}
