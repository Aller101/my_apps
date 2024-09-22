package com.springboot.restservice.service_organiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//была ошибка:
//Consider the following: If you want an embedded database (H2, HSQL or Derby),
//please put it on the classpath. If you have database settings to be loaded from
//a particular profile you may need to activate it (no profiles are currently active).
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ServiceOrganiserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceOrganiserApplication.class, args);
	}

}
