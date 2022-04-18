package org.apache.nifi.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NiFiAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiFiAgentApplication.class, args);
	}

}
