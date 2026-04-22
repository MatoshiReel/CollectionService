package ru.reel.CollectionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"ru.reel.CollectionService.repository"})
@SpringBootApplication
public class CollectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectionServiceApplication.class, args);
	}

}
