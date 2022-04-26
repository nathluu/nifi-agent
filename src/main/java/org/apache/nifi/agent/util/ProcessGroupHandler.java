package org.apache.nifi.agent.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.agent.dto.NiFiAgentParameterDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.agent.service.NiFiClient;
import org.apache.nifi.agent.service.ParamContextClient;
import org.apache.nifi.web.api.dto.ParameterContextDTO;
import org.apache.nifi.web.api.dto.ParameterDTO;
import org.apache.nifi.web.api.entity.ParameterContextEntity;
import org.apache.nifi.web.api.entity.ParameterContextUpdateRequestEntity;
import org.apache.nifi.web.api.entity.ParameterEntity;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProcessGroupHandler {
    public static void updateProcessGroupParameterContext(NiFiClient client,
                                                          ProcessGroupEntity processGroupEntity,
                                                          List<NiFiAgentParameterDTO> parameters)
            throws NiFiClientException, IOException, InterruptedException {

        if (CollectionUtils.isEmpty(parameters)) {
            return;
        }

        ParamContextClient paramContextClient = client.getParamContextClient();
        final String paramContextId = processGroupEntity.getParameterContext().getId();
        if (StringUtils.isNotBlank(paramContextId)) {
            ParameterContextEntity existingParameterContextEntity = paramContextClient.getParamContext(paramContextId, false);
            ParameterContextDTO existingParameterContextDTO = existingParameterContextEntity.getComponent();
            ParameterContextDTO parameterContextDTO = new ParameterContextDTO();
            parameterContextDTO.setId(existingParameterContextEntity.getId());
            Set<ParameterEntity> parameterEntities = new HashSet<>();
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

                if (existingParam.isPresent() && existingParam.get().getValue() != null && existingParam.get().getValue().equals(paramValue)) {
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
                parameterEntities.add(parameterEntity);
            }
            parameterContextDTO.setParameters(parameterEntities);
            ParameterContextEntity updatedParameterContextEntity = new ParameterContextEntity();
            updatedParameterContextEntity.setId(paramContextId);
            updatedParameterContextEntity.setComponent(parameterContextDTO);
            updatedParameterContextEntity.setRevision(existingParameterContextEntity.getRevision());
            ParameterContextUpdateRequestEntity updateRequestEntity = paramContextClient.updateParamContext(updatedParameterContextEntity);
            Thread.sleep(2000);
        }
    }
}
