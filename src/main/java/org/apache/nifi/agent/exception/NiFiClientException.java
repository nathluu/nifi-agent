package org.apache.nifi.agent.exception;

public class NiFiClientException extends Exception {

    public NiFiClientException(final String message) {
        super(message);
    }

    public NiFiClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

}