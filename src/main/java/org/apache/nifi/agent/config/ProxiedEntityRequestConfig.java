package org.apache.nifi.agent.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.nifi.registry.security.util.ProxiedEntitiesUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProxiedEntityRequestConfig implements RequestConfig {

    private final String[] proxiedEntities;

    public ProxiedEntityRequestConfig(final String... proxiedEntities) {
        this.proxiedEntities = Validate.notNull(proxiedEntities);
    }

    @Override
    public Map<String, String> getHeaders() {
        final String proxiedEntitiesValue = getProxiedEntitesValue(proxiedEntities);

        final Map<String,String> headers = new HashMap<>();
        if (proxiedEntitiesValue != null) {
            headers.put(ProxiedEntitiesUtils.PROXY_ENTITIES_CHAIN, proxiedEntitiesValue);
        }
        return headers;
    }

    private String getProxiedEntitesValue(final String[] proxiedEntities) {
        if (proxiedEntities == null) {
            return null;
        }

        final List<String> proxiedEntityChain = Arrays.stream(proxiedEntities)
                .map(ProxiedEntitiesUtils::formatProxyDn).collect(Collectors.toList());
        return StringUtils.join(proxiedEntityChain, "");
    }

}
