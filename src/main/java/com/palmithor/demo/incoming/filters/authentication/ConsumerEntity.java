package com.palmithor.demo.incoming.filters.authentication;

/**
 * POJO placeholder for a RestService consumer
 *
 * @author palmithor
 * @since 5/5/14.
 */
public class ConsumerEntity {

    private final String consumerId;
    private final String secret;
    private final String role;

    public ConsumerEntity(final String consumerId, final String role, final String secret) {
        this.role = role;
        this.secret = secret;
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getSecret() {
        return secret;
    }

    public String getRole() {
        return role;
    }
}
