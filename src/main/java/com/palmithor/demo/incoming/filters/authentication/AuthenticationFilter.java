package com.palmithor.demo.incoming.filters.authentication;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Example on how to use Authenticator in filter
 *
 * @author palmithor
 * @since 5/5/14.
 */
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Authenticator authenticator = new Authenticator(requestContext);
        final AuthenticationResult authenticationResult = authenticator.authenticate();
        if (!authenticationResult.isSuccess()) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build()
            );
        }
    }
}
