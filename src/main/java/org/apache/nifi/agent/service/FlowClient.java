package org.apache.nifi.agent.service;

import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.ActivateControllerServicesEntity;
import org.apache.nifi.web.api.entity.ClusteSummaryEntity;
import org.apache.nifi.web.api.entity.ConnectionStatusEntity;
import org.apache.nifi.web.api.entity.ControllerServicesEntity;
import org.apache.nifi.web.api.entity.CurrentUserEntity;
import org.apache.nifi.web.api.entity.ProcessGroupFlowEntity;
import org.apache.nifi.web.api.entity.ReportingTasksEntity;
import org.apache.nifi.web.api.entity.ScheduleComponentsEntity;
import org.apache.nifi.web.api.entity.TemplatesEntity;
import org.apache.nifi.web.api.entity.VersionedFlowSnapshotMetadataSetEntity;

import java.io.IOException;

/**
 * Client for FlowResource.
 */
public interface FlowClient {

    /**
     * @return the entity representing the current user accessing the NiFi instance
     */
    CurrentUserEntity getCurrentUser() throws NiFiClientException, IOException;

    /**
     * @return the id of the root process group
     */
    String getRootGroupId() throws NiFiClientException, IOException;

    /**
     * Retrieves the process group with the given id.
     *
     * Passing in "root" as the id will retrieve the root group.
     *
     * @param id the id of the process group to retrieve
     * @return the process group entity
     */
    ProcessGroupFlowEntity getProcessGroup(String id) throws NiFiClientException, IOException;

    /**
     * Suggest a location for the new process group on a canvas, within a given process group.
     * Will locate to the right and then bottom and offset for the size of the PG box.
     *
     * @param parentId the id of the parent process group where the new process group will be located
     * @return the process group box where a new process group can be placed
     */
    ProcessGroupBox getSuggestedProcessGroupCoordinates(String parentId) throws NiFiClientException, IOException;

    /**
     * Schedules the components of a process group.
     *
     * @param processGroupId the id of a process group
     * @param scheduleComponentsEntity the scheduled state to update to
     * @return the entity representing the scheduled state
     */
    ScheduleComponentsEntity scheduleProcessGroupComponents(
            String processGroupId, ScheduleComponentsEntity scheduleComponentsEntity) throws NiFiClientException, IOException;

    /**
     * Gets the possible versions for the given flow in the given bucket in the given registry.
     *
     * @param registryId the id of the registry client
     * @param bucketId the bucket id
     * @param flowId the flow id
     * @return the set of snapshot metadata entities
     */
    VersionedFlowSnapshotMetadataSetEntity getVersions(String registryId, String bucketId, String flowId)
            throws NiFiClientException, IOException;


    /**
     * Retrieves the controller services for the given group.
     *
     * @param groupId the process group id
     * @return tje controller services entity
     */
    ControllerServicesEntity getControllerServices(String groupId) throws NiFiClientException, IOException;

    /**
     * Enable or disable controller services in the given process group.
     *
     * @param activateControllerServicesEntity the entity indicating the process group to enable/disable services for
     * @return the activate response
     */
    ActivateControllerServicesEntity activateControllerServices(ActivateControllerServicesEntity activateControllerServicesEntity) throws NiFiClientException, IOException;

    /**
     *
     * @return cluster summary response
     */
    ClusteSummaryEntity getClusterSummary() throws NiFiClientException, IOException;

    /**
     * Retrieves the controller services for the reporting tasks.
     *
     * @return the controller services entity
     */
    ControllerServicesEntity getControllerServices() throws NiFiClientException, IOException;

    /**
     * Retrieves the reporting tasks.
     *
     * @return the reporting tasks entity
     */
    ReportingTasksEntity getReportingTasks() throws NiFiClientException, IOException;

    /**
     * Retrieves the all templates.
     *
     * @return the templates entity
     */
    TemplatesEntity getTemplates() throws NiFiClientException, IOException;

    /**
     * Retrives the status for the connection with the given ID
     * @param connectionId the id of the connection
     * @param nodewise whether or not node-wise information should be returned
     * @return the status for the connection
     */
    ConnectionStatusEntity getConnectionStatus(String connectionId, boolean nodewise) throws NiFiClientException, IOException;
}

