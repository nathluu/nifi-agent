package org.apache.nifi.agent.controller;

import org.apache.nifi.agent.dto.ProcessGroupUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
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

    @PostMapping("/process-groups/{id}/process-groups")
    ProcessGroupEntity uploadProcessGroup(@PathVariable("id") String parentGroupId, @RequestBody ProcessGroupUploadRequestDTO req)
            throws NiFiClientException, IOException {
        return client.getProcessGroupClient().uploadProcessGroup(parentGroupId, req);
    }
}
