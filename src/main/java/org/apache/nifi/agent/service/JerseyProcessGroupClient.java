package org.apache.nifi.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.agent.config.RequestConfig;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;

public class JerseyProcessGroupClient extends AbstractJerseyClient implements ProcessGroupClient {
    private final WebTarget processGroupsTarget;

    public JerseyProcessGroupClient(final WebTarget baseTarget) {
        this(baseTarget, null);
    }

    public JerseyProcessGroupClient(final WebTarget baseTarget, final RequestConfig requestConfig) {
        super(requestConfig);
        this.processGroupsTarget = baseTarget.path("/process-groups");
    }

    @Override
    public ProcessGroupEntity createProcessGroup(String parentGroupdId, ProcessGroupEntity entity) throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ProcessGroupEntity uploadProcessGroup(String parentGroupdId, ProcessGroupEntity entity) throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ProcessGroupEntity getProcessGroup(String processGroupId) throws NiFiClientException, IOException {
        if (StringUtils.isBlank(processGroupId)) {
            throw new IllegalArgumentException("Process group id cannot be null or blank");
        }

        return executeAction("Error getting process group", () -> {
            final WebTarget target = processGroupsTarget
                    .path("{id}")
                    .resolveTemplate("id", processGroupId);

            return getRequestBuilder(target).get(ProcessGroupEntity.class);
        });
    }

    @Override
    public ProcessGroupEntity updateProcessGroup(ProcessGroupEntity entity) throws NiFiClientException, IOException {
        return null;
    }
}
