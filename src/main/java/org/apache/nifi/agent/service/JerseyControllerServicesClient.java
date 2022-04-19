package org.apache.nifi.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.agent.config.RequestConfig;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.*;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;

public class JerseyControllerServicesClient extends AbstractJerseyClient implements ControllerServicesClient {
    private final WebTarget controllerServicesTarget;
    private final WebTarget processGroupTarget;

    public JerseyControllerServicesClient(final WebTarget baseTarget) {
        this(baseTarget, null);
    }

    public JerseyControllerServicesClient(final WebTarget baseTarget, final RequestConfig requestConfig) {
        super(requestConfig);
        this.controllerServicesTarget = baseTarget.path("/controller-services");
        this.processGroupTarget = baseTarget.path("/process-groups/{pgId}");
    }

    @Override
    public ControllerServiceEntity getControllerService(String id) throws NiFiClientException, IOException {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Controller service id cannot be null");
        }

        return executeAction("Error retrieving status of controller service", () -> {
            final WebTarget target = controllerServicesTarget.path("{id}").resolveTemplate("id", id);
            return getRequestBuilder(target).get(ControllerServiceEntity.class);
        });
    }

    @Override
    public ControllerServiceEntity activateControllerService(String id, ControllerServiceRunStatusEntity runStatusEntity)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ControllerServiceEntity createControllerService(String parentGroupId, ControllerServiceEntity controllerServiceEntity)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ControllerServiceEntity updateControllerService(ControllerServiceEntity controllerServiceEntity)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ControllerServiceEntity deleteControllerService(ControllerServiceEntity controllerServiceEntity)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ControllerServiceReferencingComponentsEntity getControllerServiceReferences(String id)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ControllerServiceReferencingComponentsEntity updateControllerServiceReferences(UpdateControllerServiceReferenceRequestEntity referencesEntity)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public VerifyConfigRequestEntity submitConfigVerificationRequest(VerifyConfigRequestEntity configRequestEntity)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public VerifyConfigRequestEntity getConfigVerificationRequest(String serviceId, String verificationRequestId)
            throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public VerifyConfigRequestEntity deleteConfigVerificationRequest(String serviceId, String verificationRequestId)
            throws NiFiClientException, IOException {
        return null;
    }
}
