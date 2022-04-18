package org.apache.nifi.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProcessGroupUploadRequestDTO {
    private String processGroupName;
    private Double positionX;
    private Double positionY;
    private String clientId;
    private String flowContent;
    private List<ParameterDTO> parameters;
}
