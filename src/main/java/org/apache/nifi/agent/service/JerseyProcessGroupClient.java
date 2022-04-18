package org.apache.nifi.agent.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.agent.config.RequestConfig;
import org.apache.nifi.agent.dto.ProcessGroupUploadRequestDTO;
import org.apache.nifi.agent.exception.NiFiClientException;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
    public ProcessGroupEntity createProcessGroup(String parentGroupId, ProcessGroupEntity entity) throws NiFiClientException, IOException {
        return null;
    }

    @Override
    public ProcessGroupEntity uploadProcessGroup(String parentGroupId, ProcessGroupUploadRequestDTO req)
            throws NiFiClientException, IOException {
        String decodedFlowContent = new String(Base64.getDecoder().decode(req.getFlowContent()));
        File file = File.createTempFile("flow", ".json");
        FileUtils.writeStringToFile(file, decodedFlowContent, StandardCharsets.UTF_8);
        return executeAction("Error uploading process group", () -> {
            final WebTarget target = processGroupsTarget
                    .path("{id}/process-groups/upload")
                    .resolveTemplate("id", parentGroupId);
            FileDataBodyPart filePart = new FileDataBodyPart("file", file);
            MultiPart multipart = new FormDataMultiPart()
                    .field("groupName", req.getProcessGroupName())
                    .field("positionX", req.getPositionX().toString())
                    .field("positionY", req.getPositionY().toString())
                    .field("clientId", req.getClientId())
                    .bodyPart(filePart);

            return getRequestBuilder(target).post(
                    Entity.entity(multipart, multipart.getMediaType()),
                    ProcessGroupEntity.class);
          });
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
