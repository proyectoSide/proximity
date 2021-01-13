package com.proximity;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MachineControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineControlApplication.class, args);
	}
	
	@Bean
	public ModelMapper createModel() {
		return new ModelMapper();
	}
	

}
