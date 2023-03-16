package com.marlon.blog;

import com.marlon.blog.config.DatabaseWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class BlogApplication {
	public static void main(String[] args) {

		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public CommandLineRunner writeData(DataSource dataSource) {
		return args -> {
			DatabaseWriter databaseWriter = new DatabaseWriter(dataSource);
			databaseWriter.writeData();
			System.out.println("Wrote data to PostgreSQL database!");
		};
	}
}
