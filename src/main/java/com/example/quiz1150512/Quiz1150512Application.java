package com.example.quiz1150512;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;


@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class,
		ServletWebSecurityAutoConfiguration.class })
public class Quiz1150512Application {
	

	public static void main(String[] args) {
		SpringApplication.run(Quiz1150512Application.class, args);
	}

}
