package org.apache.nifi.agent.service;

import org.apache.nifi.agent.dto.PGUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.registry.flow.VersionedFlowSnapshot;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.apache.nifi.web.api.entity.ProcessGroupsEntity;

import java.io.IOException;

public interface ProcessGroupClient {
    ProcessGroupEntity createProcessGroup(String parentGroupId, ProcessGroupEntity entity)
            throws NiFiClientException, IOException;

    ProcessGroupEntity uploadProcessGroup(String parentGroupId, PGUploadRequestDTO req)
            throws NiFiClientException, IOException;

    ProcessGroupEntity getProcessGroup(String processGroupId) throws NiFiClientException, IOException;
    ProcessGroupsEntity getChildrenProcessGroup(String processGroupId) throws NiFiClientException, IOException;

    ProcessGroupEntity updateProcessGroup(ProcessGroupEntity entity) throws NiFiClientException, IOException;
    ProcessGroupEntity deleteProcessGroup(String processGroupId, int version) throws NiFiClientException, IOException;
    VersionedFlowSnapshot downloadVersionedFlowSnapshot(String processGroupId) throws NiFiClientException, IOException;
}
