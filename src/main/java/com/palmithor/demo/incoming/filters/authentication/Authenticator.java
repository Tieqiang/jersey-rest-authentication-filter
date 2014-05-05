package com.palmithor.demo.incoming.filters.authentication;

import javax.ws.rs.container.ContainerRequestContext;

public class Authenticator {

    public static final long FIFTEEN_HOURS_AS_SECONDS = 54000L;
    private final AuthorizationParameters authParams;
    private final String method;
    private final String path;
    private final String payload;
    private final AuthInfoProviderI authInfoProvider;

    /**
     * Constructor used by filter
     *
     * @param requestContext
     */
    public Authenticator(final ContainerRequestContext requestContext) {
        this.authParams = new AuthorizationParameters(requestContext);
        this.method = requestContext.getMethod();
        this.path = requestContext.getUriInfo().getPath();
        this.payload = FilterUtil.readPayload(requestContext);
        this.authInfoProvider = new AuthInfoProviderFactory().getProvider(); // TODO create Authorization Info Provider
    }

    /**
     * Constructor used for testing
     *
     * @param authParams
     * @param method
     * @param path
     * @param payload
     */
    public Authenticator(final AuthorizationParameters authParams, final String method,
                         final String path, final String payload, final AuthInfoProviderI authInfoProvider) {
        this.authParams = authParams;
        this.method = method;
        this.path = path;
        this.payload = payload;
        this.authInfoProvider = authInfoProvider;
    }

    /**
     * Validation and authentication done for request.
     * First it is verified that the incoming consumerId + nonce + timestamp has not been sent before.
     * Next it verifies that the timestamp is inside the allowed interval which is +- fifteen hours
     * Next it verifies that the timestamp is not before the last sent message for this consumer
     * Finally the hmac signature is validated.
     *
     * @return Authentication result with either success or error and detail.
     */
    public AuthenticationResult authenticate() {
        ConsumerEntity consumer = authInfoProvider.findConsumer(authParams.getConsumer());
        if (consumer != null) {
            if (hasBeenSent()) {
                return new AuthenticationResult(AuthorizationParameters.NONCE_KEY, "Message has been sent before.");
            } else if (isTimestampOutsideAllowedInterval()) {
                return new AuthenticationResult(AuthorizationParameters.TIMESTAMP_KEY, "Is not inside allowed time frame.");
            } else if (isTimestampOlderThanLast()) {
                return new AuthenticationResult(AuthorizationParameters.TIMESTAMP_KEY, "Is invalid. Must be higher than the timestamp from latest message.");
            } else if (isInvalidSignature(consumer.getSecret())) {
                return new AuthenticationResult(AuthorizationParameters.SIGNATURE_KEY, "Signature calculations incorrect.");
            }
        } else {
            return new AuthenticationResult(AuthorizationParameters.CONSUMER_KEY, "not found.");
        }
        return new AuthenticationResult();
    }

    private boolean isInvalidSignature(final String secret) {
        SignatureParameters signatureParameters = new SignatureParameters(method, path, payload, authParams);
        String calculatedSignature = HmacUtil.calculateHmac(secret, signatureParameters);
        return !calculatedSignature.equals(authParams.getSignature());
    }

    public boolean hasBeenSent() {
        return authInfoProvider.hasBeenSent(new AccessInfoEntity(authParams.getConsumer(), authParams.getNonce(), authParams.getTimestamp()));
    }


    public boolean isTimestampOutsideAllowedInterval() {
        Long timestamp = authParams.getTimestamp();
        Long now = System.currentTimeMillis() / 1000;
        return (now - FIFTEEN_HOURS_AS_SECONDS) > timestamp || timestamp > (now + FIFTEEN_HOURS_AS_SECONDS);
    }

    public boolean isTimestampOlderThanLast() {
        Long last = authInfoProvider.findLatestConsumerTimestamp(authParams.getConsumer());
        Long timestamp = authParams.getTimestamp();
        if (last > timestamp) {
            return true;
        } else {
            return false;
        }
    }
} 