package com.proximity.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.proximity.machine.MachineStatus;

@Configuration
public class MachineConfig {

	@Autowired
	private Environment env;

	@Bean
	public MachineStatus createStatus() {

		MachineStatus status = new MachineStatus();
		status.setModel(env.getProperty("machine.model"));
		status.setPassword(env.getProperty("machine.password"));
		status.setPasswordSystem(env.getProperty("machine.admin-password"));
		
		status.setCountIntent(0);
		status.setDayIntent(new SimpleDateFormat("DD-MM-YYYY").format(new Date()));

		return status;
	}
}
