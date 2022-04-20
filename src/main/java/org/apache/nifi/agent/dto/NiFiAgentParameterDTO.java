package org.apache.nifi.agent.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NiFiAgentParameterDTO {
    private String name;
    private String value;
}
