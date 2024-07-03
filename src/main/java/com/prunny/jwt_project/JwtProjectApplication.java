package com.prunny.jwt_project;

import com.prunny.jwt_project.security.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class JwtProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtProjectApplication.class, args);
	}

}
