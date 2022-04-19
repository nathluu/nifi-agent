package org.apache.nifi.agent.service;

import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.*;

import java.io.IOException;

public interface ControllerServicesClient {
    ControllerServiceEntity getControllerService(String id) throws NiFiClientException, IOException;

    ControllerServiceEntity activateControllerService(String id, ControllerServiceRunStatusEntity runStatusEntity) throws NiFiClientException, IOException;

    ControllerServiceEntity createControllerService(String parentGroupId, ControllerServiceEntity controllerServiceEntity) throws NiFiClientException, IOException;

    ControllerServiceEntity updateControllerService(ControllerServiceEntity controllerServiceEntity) throws NiFiClientException, IOException;

    ControllerServiceEntity deleteControllerService(ControllerServiceEntity controllerServiceEntity) throws NiFiClientException, IOException;

    ControllerServiceReferencingComponentsEntity getControllerServiceReferences(String id) throws NiFiClientException, IOException;

    ControllerServiceReferencingComponentsEntity updateControllerServiceReferences(UpdateControllerServiceReferenceRequestEntity referencesEntity) throws NiFiClientException, IOException;

    VerifyConfigRequestEntity submitConfigVerificationRequest(VerifyConfigRequestEntity configRequestEntity) throws NiFiClientException, IOException;

    VerifyConfigRequestEntity getConfigVerificationRequest(String serviceId, String verificationRequestId) throws NiFiClientException, IOException;

    VerifyConfigRequestEntity deleteConfigVerificationRequest(String serviceId, String verificationRequestId) throws NiFiClientException, IOException;
}
