package org.apache.nifi.agent.service;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.agent.config.NifiClientConfig;
import org.apache.nifi.agent.config.ProxiedEntityRequestConfig;
import org.apache.nifi.agent.config.RequestConfig;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;

@Component
public class JerseyNiFiClient implements NiFiClient {
    static final String NIFI_CONTEXT = "nifi-api";
    static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    static final int DEFAULT_READ_TIMEOUT = 10000;

    private final Client client;
    private final WebTarget baseTarget;
    private final NifiClientConfig nifiClientConfig;

    @Autowired
    private JerseyNiFiClient(final NifiClientConfig conf) {
        String baseUrl = conf.getBaseUrl();
        if (StringUtils.isBlank(baseUrl)) {
            throw new IllegalArgumentException("Base URL cannot be blank");
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (!baseUrl.endsWith(NIFI_CONTEXT)) {
            baseUrl = baseUrl + "/" + NIFI_CONTEXT;
        }

        try {
            new URI(baseUrl);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Invalid base URL: " + e.getMessage(), e);
        }

        final SSLContext sslContext = conf.getSslContext();
        final ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        if (sslContext != null) {
            clientBuilder.sslContext(sslContext);
        }
        final int connectTimeout = conf.getConnectTimeout() == null ? DEFAULT_CONNECT_TIMEOUT : conf.getConnectTimeout();
        final int readTimeout = conf.getReadTimeout() == null ? DEFAULT_READ_TIMEOUT : conf.getReadTimeout();

        final ClientConfig jerseyClientConfig = new ClientConfig();
        jerseyClientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        jerseyClientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
        jerseyClientConfig.register(jacksonJaxbJsonProvider());
        jerseyClientConfig.register(MultiPartFeature.class);
        clientBuilder.withConfig(jerseyClientConfig);
        this.client = clientBuilder.build();
        this.baseTarget = client.target(baseUrl);
        this.nifiClientConfig = conf;
    }

    @Override
    public ProcessGroupClient getProcessGroupClient() {
        if (nifiClientConfig.getProxiedEntity() != null) {
            return new JerseyProcessGroupClient(baseTarget, new ProxiedEntityRequestConfig(nifiClientConfig.getProxiedEntity()));
        }
        return new JerseyProcessGroupClient(baseTarget);
    }

    @Override
    public void close() {
        if (this.client != null) {
            try {
                this.client.close();
            } catch (Exception e) {

            }
        }
    }
    private static JacksonJaxbJsonProvider jacksonJaxbJsonProvider() {
        JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL));
        mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(mapper.getTypeFactory()));
        // Ignore unknown properties so that deployed client remain compatible with future versions of NiFi that add new fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jacksonJaxbJsonProvider.setMapper(mapper);
        return jacksonJaxbJsonProvider;
    }
}
