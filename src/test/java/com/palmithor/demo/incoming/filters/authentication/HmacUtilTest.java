package com.palmithor.demo.incoming.filters.authentication;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for hmac utility.
 *
 * @author palmithor
 * @since 5/5/14.
 */
public class HmacUtilTest {


    @Test
    public void testCalculateHmacSuccess() throws Exception {
        String secret = "secret";
        AuthorizationParameters authorizationParameters = new AuthorizationParameters("consumer", "qLjYzyZ4bdSQNrcoxOal4SvHaro=", "1399323186", "nonce1");
        SignatureParameters signatureParameters = new SignatureParameters("POST", "/context/demo", "{\"key\":\"value\"}", authorizationParameters);
        assertThat(HmacUtil.calculateHmac(secret, signatureParameters), is(authorizationParameters.getSignature()));
    }
}
