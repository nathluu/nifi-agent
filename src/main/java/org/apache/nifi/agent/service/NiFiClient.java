package org.apache.nifi.agent.service;

import java.io.Closeable;

public interface NiFiClient extends Closeable {
    FlowClient getFlowClient();
    ProcessGroupClient getProcessGroupClient();
    ControllerServicesClient getControllerServicesClient();
    ParamContextClient getParamContextClient();
}
