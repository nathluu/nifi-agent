package org.apache.nifi.agent.controller;

import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.web.api.entity.ControllerServicesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class FlowController {
    private final NiFiClient client;

    @Autowired
    public FlowController(NiFiClient client) {
        this.client = client;
    }
    @GetMapping("/flow/process-groups/{id}/controller-services")
    ControllerServicesEntity getProcessGroupControllerServices(@PathVariable("id") String id) throws NiFiClientException, IOException {
        return client.getFlowClient().getControllerServices(id);

    }
}
