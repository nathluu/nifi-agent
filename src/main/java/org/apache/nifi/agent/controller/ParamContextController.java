package org.apache.nifi.agent.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.nifi.agent.dto.NiFiAgentUpdateParamContextReqDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.agent.util.ParamContextHandler;
import org.apache.nifi.web.api.entity.ParameterContextEntity;
import org.apache.nifi.web.api.entity.ParameterContextUpdateRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class ParamContextController {
    private final NiFiClient client;

    @Autowired
    public ParamContextController(NiFiClient client) {
        this.client = client;
    }

    @GetMapping("/parameter-contexts/{contextId}")
    ResponseEntity<ParameterContextEntity> getParameterContext(@PathVariable String contextId,
                                                               @RequestParam(defaultValue  = "false") boolean includeInheritedParameters)
            throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getParamContextClient().getParamContext(contextId, includeInheritedParameters));
    }

    @GetMapping("/parameter-contexts/{contextId}/update-requests/{requestId}")
    ResponseEntity<ParameterContextUpdateRequestEntity> getParamContextUpdateRequest(@PathVariable String contextId,
                                                                                     @PathVariable String requestId)
            throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getParamContextClient().getParamContextUpdateRequest(contextId, requestId));
    }

    @PostMapping("/parameter-contexts/{contextId}/update-requests")
    ResponseEntity<ParameterContextUpdateRequestEntity> createParameterContextUpdateRequest(@PathVariable String contextId,
                                                                                            @RequestBody NiFiAgentUpdateParamContextReqDTO req)
            throws NiFiClientException, IOException, InterruptedException {
        ParameterContextEntity updatedParameterContextEntity = ParamContextHandler.createParamContextUpdateReq(client, contextId, req);
        return ResponseEntity.ok(client.getParamContextClient().updateParamContext(updatedParameterContextEntity));
    }

    @DeleteMapping("/parameter-contexts/{contextId}/update-requests/{requestId}")
    ResponseEntity<ParameterContextUpdateRequestEntity> deleteParameterContextUpdateRequest(@PathVariable String contextId,
                                                                                            @PathVariable String requestId) throws NiFiClientException, IOException {
        return ResponseEntity.ok(client.getParamContextClient().deleteParamContextUpdateRequest(contextId, requestId));
    }
}
