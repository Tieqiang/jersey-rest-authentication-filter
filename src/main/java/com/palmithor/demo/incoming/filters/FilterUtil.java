package com.palmithor.demo.incoming.filters;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility for jersey filters
 *
 * @author palmithor
 * @since 5/5/14.
 */
public final class FilterUtil {

    private static Logger logger = LoggerFactory.getLogger(FilterUtil.class);

    private FilterUtil() {
    }

    public static String readPayload(final ContainerRequestContext requestContext) {
        try {
            String payload = IOUtils.toString(requestContext.getEntityStream(), Charsets.UTF_8);
            InputStream in = IOUtils.toInputStream(payload);
            requestContext.setEntityStream(in);
            return payload;
        } catch (IOException e) {
            logger.info("Failed while reading entity stream", e);
            return "";
        }
    }
}
