package org.apache.nifi.agent.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.nifi.web.api.dto.PositionDTO;
import org.apache.nifi.web.api.dto.RevisionDTO;

@Getter
@Setter
public class NiFiAgentProcessGroupDTO {
    private String id;
    private String name;
    private PositionDTO position;
    private RevisionDTO revision;
}
