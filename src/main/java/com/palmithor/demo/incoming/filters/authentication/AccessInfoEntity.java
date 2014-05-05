package com.palmithor.demo.incoming.filters.authentication;

/**
 * POJO placeholder for access information. This is then used to prevent reply attacks.
 *
 * @author palmithor
 * @since 5/5/14.
 */
public class AccessInfoEntity {

    private final String consumerId;
    private final String nonce;
    private final Long timestamp;

    public AccessInfoEntity(final String consumerId, final String nonce, final Long timestamp) {
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getNonce() {
        return nonce;
    }
}
