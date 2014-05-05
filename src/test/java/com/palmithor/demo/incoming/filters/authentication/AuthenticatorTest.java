package com.palmithor.demo.incoming.filters.authentication;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author palmithor
 * @since 5/5/14.
 */
public class AuthenticatorTest {


    public static final String DUMMY_VALUE = "123";
    public static final String METHOD_POST = "POST";
    public static final String DUMMY_PATH = "/context/demo";
    public static final String DUMMY_PAYLOAD = "{\"key\":\"value\"}";

    @Test
    public void testAuthenticateNotFoundConsumer() {
        AuthorizationParameters authParams = new AuthorizationParameters("NOT_FOUND", DUMMY_VALUE, DUMMY_VALUE, DUMMY_VALUE);
        Authenticator authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, "{\"key\":\"value\"}", new StaticAuthInfoProvider());
        AuthenticationResult result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.FALSE));
        assertThat(result.getErrorDetailKey(), is(AuthorizationParameters.CONSUMER_KEY));
        assertThat(result.getErrorDetailValue(), is("not found."));
    }

    @Test
    public void testAuthenticateHasBeenSent() {
        AuthorizationParameters authParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, DUMMY_VALUE, String.valueOf(StaticAuthInfoProvider.TIMESTAMP), StaticAuthInfoProvider.NONCE);
        Authenticator authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, new StaticAuthInfoProvider());
        AuthenticationResult result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.FALSE));
        assertThat(result.getErrorDetailKey(), is(AuthorizationParameters.NONCE_KEY));
        assertThat(result.getErrorDetailValue(), is("Message has been sent before."));
    }

    @Test
    public void testAuthenticateTimeStampOutsideInterval() {
        // Lower than minimum
        AuthorizationParameters authParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, DUMMY_VALUE, String.valueOf(System.currentTimeMillis() / 1000 - 54003), StaticAuthInfoProvider.NONCE);
        Authenticator authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, new StaticAuthInfoProvider());
        AuthenticationResult result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.FALSE));
        assertThat(result.getErrorDetailKey(), is(AuthorizationParameters.TIMESTAMP_KEY));
        assertThat(result.getErrorDetailValue(), is("Is not inside allowed time frame."));

        // greater than maximum
        authParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, DUMMY_VALUE, String.valueOf(System.currentTimeMillis() / 1000 + 54003), StaticAuthInfoProvider.NONCE);
        authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, new StaticAuthInfoProvider());
        result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.FALSE));
        assertThat(result.getErrorDetailKey(), is(AuthorizationParameters.TIMESTAMP_KEY));
        assertThat(result.getErrorDetailValue(), is("Is not inside allowed time frame."));
    }

    @Test
    public void testIsTimestampOlderThanLast() {
        AuthorizationParameters authParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, DUMMY_VALUE, String.valueOf(StaticAuthInfoProvider.TIMESTAMP - 1), StaticAuthInfoProvider.NONCE);
        Authenticator authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, new StaticAuthInfoProvider());
        AuthenticationResult result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.FALSE));
        assertThat(result.getErrorDetailKey(), is(AuthorizationParameters.TIMESTAMP_KEY));
        assertThat(result.getErrorDetailValue(), is("Is invalid. Must be higher than the timestamp from latest message."));
    }

    @Test
    public void testInvalidSignature() {
        AuthorizationParameters authParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, DUMMY_VALUE, String.valueOf(System.currentTimeMillis() / 1000), StaticAuthInfoProvider.NONCE);
        Authenticator authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, new StaticAuthInfoProvider());
        AuthenticationResult result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.FALSE));
        assertThat(result.getErrorDetailKey(), is(AuthorizationParameters.SIGNATURE_KEY));
        assertThat(result.getErrorDetailValue(), is("Signature calculations incorrect."));
    }

    @Test
    public void testAuthenticationSuccess() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = String.valueOf(System.currentTimeMillis());
        AuthorizationParameters clientAuthorizationParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, timestamp, nonce);
        SignatureParameters clientSignatureParameters = new SignatureParameters(METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, clientAuthorizationParams);
        String signature = HmacUtil.calculateHmac(StaticAuthInfoProvider.SECRET, clientSignatureParameters);
        AuthorizationParameters authParams = new AuthorizationParameters(StaticAuthInfoProvider.CONSUMER_ADMIN, signature, timestamp, nonce);
        Authenticator authenticator = new Authenticator(authParams, METHOD_POST, DUMMY_PATH, DUMMY_PAYLOAD, new StaticAuthInfoProvider());
        AuthenticationResult result = authenticator.authenticate();
        assertThat(result.isSuccess(), is(Boolean.TRUE));
        assertThat(result.getErrorDetailKey(), is(nullValue()));
        assertThat(result.getErrorDetailValue(), is(nullValue()));
    }
}
