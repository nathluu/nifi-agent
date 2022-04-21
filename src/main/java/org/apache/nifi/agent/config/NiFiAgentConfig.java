package org.apache.nifi.agent.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NiFiAgentConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
