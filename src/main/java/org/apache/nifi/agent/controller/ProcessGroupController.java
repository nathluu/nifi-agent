package org.apache.nifi.agent.controller;

import org.apache.nifi.agent.dto.ProcessGroupUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.apache.nifi.web.api.entity.ProcessGroupsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class ProcessGroupController {
    private final NiFiClient client;

    @Autowired
    public ProcessGroupController(NiFiClient client) {
        this.client = client;
    }

    @GetMapping("/process-groups/{id}")
    ProcessGroupEntity getProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return client.getProcessGroupClient().getProcessGroup(id);
    }

    @GetMapping("/process-groups/{id}/process-groups")
    ProcessGroupsEntity getAllChildrenProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return client.getProcessGroupClient().getChildrenProcessGroup(id);
    }

    @PostMapping("/process-groups/{id}/process-groups")
    ProcessGroupEntity uploadProcessGroup(@PathVariable("id") String parentGroupId, @RequestBody ProcessGroupUploadRequestDTO req)
            throws NiFiClientException, IOException {
        return client.getProcessGroupClient().uploadProcessGroup(parentGroupId, req);
    }
}
