package org.apache.nifi.agent.service;

import java.io.Closeable;

public interface NiFiClient extends Closeable {
    ProcessGroupClient getProcessGroupClient();
}
