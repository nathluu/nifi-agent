package org.apache.nifi.agent.util;

import org.apache.nifi.agent.dto.NiFiAgentProcessGroupDTO;
import org.apache.nifi.agent.dto.NiFiAgentProcessGroupsDTO;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.apache.nifi.web.api.entity.ProcessGroupsEntity;

import java.util.stream.Collectors;

public class Converter {

    public static NiFiAgentProcessGroupDTO convertProcessGroupEntityToDto(ProcessGroupEntity entity) {
        NiFiAgentProcessGroupDTO pgDto = new NiFiAgentProcessGroupDTO();
        pgDto.setId(entity.getComponent().getId());
        pgDto.setName(entity.getComponent().getName());
        pgDto.setRevision(entity.getRevision());
        pgDto.setPosition(entity.getPosition());
        return pgDto;
    }

    public static NiFiAgentProcessGroupsDTO convertProcessGroupsEntityToDto(ProcessGroupsEntity entity) {
        NiFiAgentProcessGroupsDTO pgsDto = new NiFiAgentProcessGroupsDTO();
        pgsDto.setProcessGroups(entity.getProcessGroups().stream()
                .map(Converter::convertProcessGroupEntityToDto)
                .collect(Collectors.toSet()));
        return pgsDto;
    }
}
