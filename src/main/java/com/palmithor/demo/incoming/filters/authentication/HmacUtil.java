package com.palmithor.demo.incoming.filters.authentication;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Utility used to calculate HMAC.
 *
 * @author palmithor
 * @since 5/5/14.
 */
public final class HmacUtil {

    private static final Logger logger = LoggerFactory.getLogger(HmacUtil.class);

    private HmacUtil() {
    }

    public static String calculateHmac(final String secret, final SignatureParameters signatureParameters) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), signatureParameters.getAuthParams().getSignatureMethod());
            Mac mac = Mac.getInstance(signatureParameters.getAuthParams().getSignatureMethod());
            mac.init(keySpec);
            byte[] signatureAsBytes = signatureParameters.constructSignatureBaseString().getBytes(Charsets.UTF_8);
            byte[] result = mac.doFinal(signatureAsBytes);
            return new BASE64Encoder().encode(result);
        } catch (NoSuchAlgorithmException e) {
            logger.info("Consumer '" + signatureParameters.getAuthParams().getConsumer() + "' used an invalid algorithm (" + signatureParameters.getAuthParams().getSignatureMethod() + ")", e);
        } catch (InvalidKeyException e) {
            logger.info("Invalid key when initializing mac", e);
        }
        return "";
    }
}
