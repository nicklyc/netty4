package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.server.ServerInitializer;

@SpringBootApplication()
public class TcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcpServerApplication.class, args);
	}
}
