package org.apache.nifi.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PGUploadRequestDTO {
    private String processGroupName;
    private Double positionX;
    private Double positionY;
    private String clientId;
    private String flowContent;
    private List<NiFiAgentParameterDTO> parameters;
}
