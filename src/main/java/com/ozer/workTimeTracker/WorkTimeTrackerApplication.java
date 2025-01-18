package com.ozer.workTimeTracker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class WorkTimeTrackerApplication {


	public static void main(String[] args) {
		SpringApplication.run(WorkTimeTrackerApplication.class, args);
	}

	@Value("${cors.allowed.origins}")
	private String allowedOrigins;

	@Value("${spring.datasource.url}")
	private String databaseUrl;

	@Value("${spring.datasource.username}")
	private String databaseUser;

	@Value("${spring.datasource.password}")
	private String databasePW;

	@PostConstruct
	public void printApplicationProperties() {
		System.out.println("alowedOrigins: " + allowedOrigins);
		System.out.println("databaseUrl: " + databaseUrl);
		System.out.println("databaseUser: " + databaseUser);
		System.out.println("databasePW: " + databasePW);
	}

}
