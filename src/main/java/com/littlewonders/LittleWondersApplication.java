package com.littlewonders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LittleWondersApplication
		extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {

	@Override
	protected org.springframework.boot.builder.SpringApplicationBuilder configure(
			org.springframework.boot.builder.SpringApplicationBuilder application) {
		return application.sources(LittleWondersApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LittleWondersApplication.class, args);
	}

}
