package org.apache.nifi.agent.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.nifi.agent.dto.PGUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.registry.flow.VersionedFlowSnapshot;
import org.apache.nifi.web.api.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    ResponseEntity<ProcessGroupEntity> getProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getProcessGroupClient().getProcessGroup(id));
    }

    @DeleteMapping("/process-groups/{id}")
    ResponseEntity<ProcessGroupEntity> deleteProcessGroup(@PathVariable("id") String id,
                                                @RequestParam int version) throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getProcessGroupClient().deleteProcessGroup(id, version));
    }

    @GetMapping("/process-groups/{id}/process-groups")
    ResponseEntity<ProcessGroupsEntity> getAllChildrenProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getProcessGroupClient().getChildrenProcessGroup(id));
    }

    @PostMapping("/process-groups/{id}/process-groups/upload")
    ResponseEntity<ProcessGroupEntity> uploadProcessGroup(@PathVariable("id") String parentGroupId, @RequestBody PGUploadRequestDTO req)
            throws NiFiClientException, IOException, InterruptedException {
        return ResponseEntity.ok(client.getProcessGroupClient().uploadProcessGroup(parentGroupId, req));
    }

    @GetMapping("/process-groups/{id}/download")
    ResponseEntity<VersionedFlowSnapshot> downloadProcessGroup(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getProcessGroupClient().downloadVersionedFlowSnapshot(id));
    }
}
