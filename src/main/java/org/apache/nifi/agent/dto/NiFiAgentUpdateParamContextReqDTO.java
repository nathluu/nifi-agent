package org.apache.nifi.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NiFiAgentUpdateParamContextReqDTO {
    private List<NiFiAgentParameterDTO> parameters;
}
