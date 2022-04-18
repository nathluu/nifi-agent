package org.apache.nifi.nifiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NifiAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(NifiAgentApplication.class, args);
	}

}
