package xyz.sadiulhakim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfiguration {

	@Bean
	Executor defaultTaskExecutor() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}

}
