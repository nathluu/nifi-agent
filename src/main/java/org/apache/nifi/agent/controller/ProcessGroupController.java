package org.apache.nifi.agent.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.nifi.agent.dto.NiFiAgentParameterDTO;
import org.apache.nifi.agent.dto.NiFiAgentProcessGroupDTO;
import org.apache.nifi.agent.dto.NiFiAgentProcessGroupsDTO;
import org.apache.nifi.agent.dto.PGUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.agent.service.ParamContextClient;
import org.apache.nifi.agent.util.Converter;
import org.apache.nifi.web.api.dto.ParameterContextDTO;
import org.apache.nifi.web.api.dto.ParameterDTO;
import org.apache.nifi.web.api.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        final ParamContextClient paramContextClient = client.getParamContextClient();
        final ProcessGroupEntity result = client.getProcessGroupClient().uploadProcessGroup(parentGroupId, req);
        final String paramContextId = result.getParameterContext().getId();
        ParameterContextEntity existingParameterContextEntity = paramContextClient.getParamContext(paramContextId, false);
        ParameterContextDTO existingParameterContextDTO = existingParameterContextEntity.getComponent();
        List<NiFiAgentParameterDTO> parameters = req.getParameters();
        for (NiFiAgentParameterDTO param : parameters) {
            String paramName = param.getName();
            String paramValue = param.getValue();
            Optional<ParameterDTO> existingParam = existingParameterContextDTO.getParameters().stream()
                    .map(ParameterEntity::getParameter)
                    .filter(p -> p.getName().equals(paramName))
                    .findFirst();

            if (!existingParam.isPresent() && paramValue == null) {
                throw new IllegalArgumentException("A parameter value is required when creating a new parameter");
            }

            if (existingParam.isPresent() && existingParam.get().getValue().equals(paramValue)) {
//                throw new IllegalArgumentException(String.format("Parameter value supplied for parameter [%s] is the same as its current value", paramName));
                continue;
            }
            // Construct the objects for the update...
            ParameterDTO parameterDTO = existingParam.isPresent() ? existingParam.get() : new ParameterDTO();
            parameterDTO.setName(paramName);
            if (paramValue != null) {
                parameterDTO.setValue(paramValue);
            }

            ParameterEntity parameterEntity = new ParameterEntity();
            parameterEntity.setParameter(parameterDTO);
            ParameterContextDTO parameterContextDTO = new ParameterContextDTO();
            parameterContextDTO.setId(existingParameterContextEntity.getId());
            parameterContextDTO.setParameters(Collections.singleton(parameterEntity));

            ParameterContextEntity updatedParameterContextEntity = new ParameterContextEntity();
            updatedParameterContextEntity.setId(paramContextId);
            updatedParameterContextEntity.setComponent(parameterContextDTO);
            updatedParameterContextEntity.setRevision(existingParameterContextEntity.getRevision());
            ParameterContextUpdateRequestEntity updateRequestEntity = paramContextClient.updateParamContext(updatedParameterContextEntity);
            Thread.sleep(2000);
        }

        return Converter.convertProcessGroupEntityToDto(client.getProcessGroupClient().getProcessGroup(result.getId()));
    }
}
