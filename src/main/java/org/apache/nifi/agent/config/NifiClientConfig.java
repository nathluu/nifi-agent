package org.apache.nifi.agent.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.nifi.security.util.TlsConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.nifi.registry.security.util.KeyStoreUtils;
import org.apache.nifi.registry.security.util.KeystoreType;
import org.apache.nifi.security.util.TlsConfiguration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "nifi.client")
public class NifiClientConfig {
    public static final String DEFAULT_PROTOCOL = TlsConfiguration.getHighestCurrentSupportedTlsProtocolVersion();

    private String baseUrl;
    private SSLContext sslContext;
    private String keystoreFilename;
    private String keystorePass;
    private String keyPass;
    private KeystoreType keystoreType;
    private String truststoreFilename;
    private String truststorePass;
    private KeystoreType truststoreType;
    private String protocol;
    private HostnameVerifier hostnameVerifier;
    private Integer readTimeout;
    private Integer connectTimeout;
    private String proxiedEntity;

    public SSLContext getSslContext() {
        if (sslContext != null) {
            return sslContext;
        }

        final KeyManagerFactory keyManagerFactory;
        if (keystoreFilename != null && keystorePass != null && keystoreType != null) {
            try {
                // prepare the keystore
                final KeyStore keyStore = KeyStoreUtils.getKeyStore(keystoreType.name());
                try (final InputStream keyStoreStream = Files.newInputStream(new File(keystoreFilename).toPath())) {
                    keyStore.load(keyStoreStream, keystorePass.toCharArray());
                }
                keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

                if (keyPass == null) {
                    keyManagerFactory.init(keyStore, keystorePass.toCharArray());
                } else {
                    keyManagerFactory.init(keyStore, keyPass.toCharArray());
                }
            } catch (final Exception e) {
                throw new IllegalStateException("Failed to load Keystore", e);
            }
        } else {
            keyManagerFactory = null;
        }

        final TrustManagerFactory trustManagerFactory;
        if (truststoreFilename != null && truststorePass != null && truststoreType != null) {
            try {
                // prepare the truststore
                final KeyStore trustStore = KeyStoreUtils.getKeyStore(truststoreType.name());
                try (final InputStream trustStoreStream = Files.newInputStream(new File(truststoreFilename).toPath())) {
                    trustStore.load(trustStoreStream, truststorePass.toCharArray());
                }
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
            } catch (final Exception e) {
                throw new IllegalStateException("Failed to load Truststore", e);
            }
        } else {
            trustManagerFactory = null;
        }

        if (keyManagerFactory != null || trustManagerFactory != null) {
            try {
                // initialize the ssl context
                KeyManager[] keyManagers = keyManagerFactory != null ? keyManagerFactory.getKeyManagers() : null;
                TrustManager[] trustManagers = trustManagerFactory != null ? trustManagerFactory.getTrustManagers() : null;
                final SSLContext sslContext = SSLContext.getInstance(DEFAULT_PROTOCOL);
//                final SSLContext sslContext = SSLContext.getInstance(getProtocol());
                sslContext.init(keyManagers, trustManagers, new SecureRandom());
                sslContext.getDefaultSSLParameters().setNeedClientAuth(true);

                return sslContext;
            } catch (final Exception e) {
                throw new IllegalStateException("Created keystore and truststore but failed to initialize SSLContext", e);
            }
        } else {
            return null;
        }
    }
}
