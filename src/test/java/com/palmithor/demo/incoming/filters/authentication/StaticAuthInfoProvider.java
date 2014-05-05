package com.palmithor.demo.incoming.filters.authentication;

import java.util.ArrayList;

/**
 * Demo implementation of AuthorizationInfoProvider. All data is static.
 *
 * @author palmithor
 * @since 5/5/14.
 */
public class StaticAuthInfoProvider implements AuthInfoProviderI {

    public static final Long TIMESTAMP = 1399323186L;
    public static final String CONSUMER_ADMIN = "admin";
    public static final String CONSUMER_USER_1 = "user1";
    public static final String CONSUMER_USER_2 = "user2";
    public static final String CONSUMER_USER_3 = "user3";
    public static final String SECRET = "secret";
    public static final String NONCE = "nonce1";


    ArrayList<ConsumerEntity> consumers = new ArrayList<ConsumerEntity>() {{
        add(new ConsumerEntity(CONSUMER_ADMIN, CONSUMER_ADMIN, SECRET));
        add(new ConsumerEntity(CONSUMER_USER_1, "operator", SECRET));
        add(new ConsumerEntity(CONSUMER_USER_2, "operator", SECRET));
        add(new ConsumerEntity(CONSUMER_USER_3, "viewer", SECRET));
    }};

    ArrayList<AccessInfoEntity> accessInfoList = new ArrayList<AccessInfoEntity>() {{
        add(new AccessInfoEntity(CONSUMER_ADMIN, NONCE, TIMESTAMP));
        add(new AccessInfoEntity(CONSUMER_USER_1, NONCE, TIMESTAMP));
        add(new AccessInfoEntity(CONSUMER_USER_2, NONCE, TIMESTAMP));
        add(new AccessInfoEntity(CONSUMER_USER_3, NONCE, TIMESTAMP));
    }};

    @Override
    public ConsumerEntity findConsumer(final String consumerId) {
        for (ConsumerEntity c : consumers) {
            if (c.getConsumerId().equals(consumerId)) {
                return c;
            }
        }
        return null;
    }


    @Override
    public boolean hasBeenSent(final AccessInfoEntity accessInfo) {
        for (AccessInfoEntity a : accessInfoList) {
            if (accessInfo.getConsumerId().equals(a.getConsumerId()) &&
                    accessInfo.getTimestamp().equals(a.getTimestamp()) &&
                    accessInfo.getNonce().equals(a.getNonce())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Long findLatestConsumerTimestamp(String consumerId) {
        Long max = 0L;
        for (AccessInfoEntity a : accessInfoList) {
            if (a.getConsumerId().equals(consumerId)) {
                if (a.getTimestamp() > max) {
                    max = a.getTimestamp();
                }
            }
        }
        return max;
    }
}
