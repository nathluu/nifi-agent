package org.apache.nifi.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class NiFiAgentProcessGroupsDTO {
    private Set<NiFiAgentProcessGroupDTO> processGroups;
}
