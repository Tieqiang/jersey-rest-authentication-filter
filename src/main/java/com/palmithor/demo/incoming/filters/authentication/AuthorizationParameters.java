package com.palmithor.demo.incoming.filters.authentication;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationParameters {

    private static final String SEP = "&";

    public static final String CONSUMER_KEY = "hp_consumer_key";
    public static final String SIGNATURE_KEY = "hp_auth_signature";
    public static final String TIMESTAMP_KEY = "hp_auth_timestamp";
    public static final String NONCE_KEY = "hp_auth_nonce";
    public static final String VERSION_KEY = "hp_auth_version";
    public static final String SIGNATURE_METHOD_KEY = "hp_auth_signature_method";
    public static final String HMAC_SHA1 = "HmacSHA1";

    private final String consumer;
    private final String signature;
    private final String timestamp;
    private final String nonce;
    private final String version;
    private final String signatureMethod;

    /**
     * Constructor used for server testing
     *
     * @param consumer
     * @param signature
     * @param timestamp
     * @param nonce
     */
    public AuthorizationParameters(final String consumer, final String signature, final String timestamp, final String nonce) {
        this.consumer = consumer;
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.version = "1";
        this.signatureMethod = HMAC_SHA1;
    }

    /**
     * Constructor used by client
     *
     * @param consumer
     * @param timestamp
     * @param nonce
     */
    public AuthorizationParameters(final String consumer, final String timestamp, final String nonce) {
        this.consumer = consumer;
        this.signature = null;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.version = "1";
        this.signatureMethod = HMAC_SHA1;
    }

    /**
     * Constructor used by server
     *
     * @param requestContext
     */
    public AuthorizationParameters(final ContainerRequestContext requestContext) {
        this.consumer = requestContext.getHeaderString(CONSUMER_KEY);
        this.signature = requestContext.getHeaderString(SIGNATURE_KEY);
        this.timestamp = requestContext.getHeaderString(TIMESTAMP_KEY);
        this.nonce = requestContext.getHeaderString(NONCE_KEY);
        this.version = "1";
        this.signatureMethod = HMAC_SHA1;
    }

    /**
     * Method validates that the headers include all necessary Handpoint
     * authorization headers.
     *
     * @returns empty map if no errors, otherwise a map with the errors
     */
    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<String, String>();

        // extract to valiate consumer key
        if (StringUtils.isBlank(consumer)) {
            errors.put(CONSUMER_KEY, "is required");
        }

        // extract to valiate signature
        if (StringUtils.isBlank(signature)) {
            errors.put(SIGNATURE_KEY, "is required");
        }

        // extract to validate timestamp
        // Unless otherwise specified by the Service Provider,
        // the timestamp is expressed in the number of seconds since
        // January 1, 1970 00:00:00 GMT. The timestamp value MUST be a
        // positive integer and MUST be equal or greater than the timestamp used
        // in previous requests.
        if (StringUtils.isBlank(timestamp)) {
            errors.put(TIMESTAMP_KEY, "is required");
        } else if (!NumberUtils.isNumber(timestamp)) {
            errors.put(TIMESTAMP_KEY, "is invalid");
        }

        // The Consumer SHALL then generate a Nonce value that is unique for all
        // requests with that timestamp. A nonce is a random string, uniquely
        // generated for each request. The nonce allows the Service Provider to
        // verify that a request has never been made before and helps prevent
        //replay attacks when requests are made over a non-secure channel (such as HTTP).
        if (StringUtils.isBlank(nonce)) {
            errors.put(NONCE_KEY, "is required");
        }

        return errors;
    }

    public String constructSignatureString() {
        StringBuilder builder = new StringBuilder();
        builder.append(CONSUMER_KEY).append("=").append(consumer).append(SEP);
        builder.append(TIMESTAMP_KEY).append("=").append(timestamp).append(SEP);
        builder.append(NONCE_KEY).append("=").append(nonce);
        return builder.toString();
    }

    public String getConsumer() {
        return consumer;
    }

    public String getSignature() {
        return signature;
    }

    public Long getTimestamp() {
        return Long.valueOf(timestamp);
    }

    public String getNonce() {
        return nonce;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }
}