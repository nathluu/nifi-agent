package org.apache.nifi.agent.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.agent.dto.NiFiAgentParameterDTO;
import org.apache.nifi.agent.dto.NiFiAgentProcessGroupDTO;
import org.apache.nifi.agent.dto.NiFiAgentProcessGroupsDTO;
import org.apache.nifi.agent.dto.PGUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.agent.service.ParamContextClient;
import org.apache.nifi.agent.util.Converter;
import org.apache.nifi.agent.util.ProcessGroupHandler;
import org.apache.nifi.web.api.dto.ParameterContextDTO;
import org.apache.nifi.web.api.dto.ParameterDTO;
import org.apache.nifi.web.api.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class ProcessGroupController {
    private final NiFiClient client;

    @Autowired
    public ProcessGroupController(NiFiClient client) {
        this.client = client;
    }

    @GetMapping("/process-groups/{id}")
    NiFiAgentProcessGroupDTO getProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return Converter.convertProcessGroupEntityToDto(client.getProcessGroupClient().getProcessGroup(id));
    }

    @GetMapping("/process-groups/{id}/process-groups")
    NiFiAgentProcessGroupsDTO getAllChildrenProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return Converter.convertProcessGroupsEntityToDto(client.getProcessGroupClient().getChildrenProcessGroup(id));
    }

    @PostMapping("/process-groups/{id}/process-groups/upload")
    NiFiAgentProcessGroupDTO uploadProcessGroup(@PathVariable("id") String parentGroupId, @RequestBody PGUploadRequestDTO req)
            throws NiFiClientException, IOException, InterruptedException {
        final ProcessGroupEntity result = client.getProcessGroupClient().uploadProcessGroup(parentGroupId, req);
        ProcessGroupHandler.updateProcessGroupParameterContext(client, result, req.getParameters());
        return Converter.convertProcessGroupEntityToDto(client.getProcessGroupClient().getProcessGroup(result.getId()));
    }
}
