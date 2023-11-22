package com.example.newsfeed_8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NewsFeed8Application {

	public static void main(String[] args) {
		SpringApplication.run(NewsFeed8Application.class, args);
	}

}
