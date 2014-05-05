package com.palmithor.demo.incoming.filters.authentication;

/**
 * Interface for Authentication Data Provider.
 * <p/>
 * Classes that implement this can provide information are needed for authentication.
 *
 * @author palmithor
 * @since 5/5/14.
 */
public interface AuthInfoProviderI {

    ConsumerEntity findConsumer(final String consumerId);

    boolean hasBeenSent(final AccessInfoEntity accessInfo);

    Long findLatestConsumerTimestamp(final String consumerId);

}
