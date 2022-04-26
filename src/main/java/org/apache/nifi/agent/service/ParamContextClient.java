package org.apache.nifi.agent.service;

import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.ParameterContextEntity;
import org.apache.nifi.web.api.entity.ParameterContextUpdateRequestEntity;
import org.apache.nifi.web.api.entity.ParameterContextsEntity;

import java.io.IOException;

public interface ParamContextClient {

    ParameterContextsEntity getParamContexts() throws NiFiClientException, IOException;

    ParameterContextEntity getParamContext(String id, boolean includeInheritedParameters) throws NiFiClientException, IOException;

    ParameterContextEntity createParamContext(ParameterContextEntity paramContext) throws NiFiClientException, IOException;

    ParameterContextEntity deleteParamContext(String id, String version) throws NiFiClientException, IOException;

    ParameterContextUpdateRequestEntity updateParamContext(ParameterContextEntity paramContext) throws NiFiClientException, IOException;

    ParameterContextUpdateRequestEntity getParamContextUpdateRequest(String contextId, String updateRequestId) throws NiFiClientException, IOException;

    ParameterContextUpdateRequestEntity deleteParamContextUpdateRequest(String contextId, String updateRequestId) throws NiFiClientException, IOException;

}
