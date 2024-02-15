package com.govtech.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GitHubStatusTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubStatusTrackerApplication.class, args);
	}

}
