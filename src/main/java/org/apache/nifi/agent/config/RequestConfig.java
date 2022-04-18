package org.apache.nifi.agent.config;

import java.util.Map;

public interface RequestConfig {
    /**
     * @return the headers to apply to each request
     */
    Map<String,String> getHeaders();
}
