package org.example.discussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for Discussion Service
 * Now scans both core and common modules
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "org.example.discussion",
    "org.example.backend.core",
    "org.example.backend.common"
})
@EntityScan(basePackages = {
    "org.example.discussion.domain",
    "org.example.backend.core.domain"
})
@EnableJpaRepositories(basePackages = {
    "org.example.discussion.repository",
    "org.example.backend.core.repository"
})
@EnableMongoRepositories
@EnableAsync
public class DiscussionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscussionApplication.class, args);
		System.out.println("Discussion Service Started Successfully!");
	}

}
