package com.palmithor.demo.incoming.filters.authentication;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author palmithor
 * @since 5/5/14.
 */
public class AuthorizationParametersTest {

    @Test
    public void testValidateAllBlankOrNull() {
        AuthorizationParameters authParams = new AuthorizationParameters("", "", "", "");
        Map<String, String> errors = authParams.validate();
        assertThat(errors.size(), is(4));
        authParams = new AuthorizationParameters(null, null, null, null);
        errors = authParams.validate();
        assertThat(errors.size(), is(4));
    }

    @Test
    public void testValidateTimestampNotNumber() {
        AuthorizationParameters authParams = new AuthorizationParameters("a", "b", "c", "d");
        Map<String, String> errors = authParams.validate();
        assertThat(errors.size(), is(1));
        assertThat(errors.get(AuthorizationParameters.TIMESTAMP_KEY), is("is invalid"));
    }

    @Test
    public void testConstructSignatureString() {
        AuthorizationParameters authParams = new AuthorizationParameters("a", "b", "c", "1399325718");
        assertThat(authParams.constructSignatureString(), is("hp_consumer_key=a&hp_auth_timestamp=c&hp_auth_nonce=1399325718"));
    }
}
