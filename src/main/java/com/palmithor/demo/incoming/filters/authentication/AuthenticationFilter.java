package com.palmithor.demo.incoming.filters.authentication;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Example on how to use Authenticator in filter
 *
 * @author palmithor
 * @since 5/5/14.
 */
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Authenticator authenticator = new Authenticator(containerRequestContext);
    }
}
