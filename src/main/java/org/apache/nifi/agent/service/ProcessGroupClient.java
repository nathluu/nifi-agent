package org.apache.nifi.agent.service;

import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;

import java.io.IOException;

public interface ProcessGroupClient {
    ProcessGroupEntity createProcessGroup(String parentGroupdId, ProcessGroupEntity entity)
            throws NiFiClientException, IOException;

    ProcessGroupEntity uploadProcessGroup(String parentGroupdId, ProcessGroupEntity entity)
            throws NiFiClientException, IOException;

    ProcessGroupEntity getProcessGroup(String processGroupId) throws NiFiClientException, IOException;

    ProcessGroupEntity updateProcessGroup(ProcessGroupEntity entity) throws NiFiClientException, IOException;
}
