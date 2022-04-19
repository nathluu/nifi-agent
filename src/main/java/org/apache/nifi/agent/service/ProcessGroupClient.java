package org.apache.nifi.agent.service;

import org.apache.nifi.agent.dto.ProcessGroupUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.apache.nifi.web.api.entity.ProcessGroupsEntity;

import java.io.IOException;
import java.util.Set;

public interface ProcessGroupClient {
    ProcessGroupEntity createProcessGroup(String parentGroupId, ProcessGroupEntity entity)
            throws NiFiClientException, IOException;

    ProcessGroupEntity uploadProcessGroup(String parentGroupId, ProcessGroupUploadRequestDTO req)
            throws NiFiClientException, IOException;

    ProcessGroupEntity getProcessGroup(String processGroupId) throws NiFiClientException, IOException;
    ProcessGroupsEntity getChildrenProcessGroup(String processGroupId) throws NiFiClientException, IOException;

    ProcessGroupEntity updateProcessGroup(ProcessGroupEntity entity) throws NiFiClientException, IOException;
}
