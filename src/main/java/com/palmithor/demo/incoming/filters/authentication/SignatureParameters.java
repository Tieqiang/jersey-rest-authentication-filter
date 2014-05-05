package com.palmithor.demo.incoming.filters.authentication;

import org.apache.commons.lang3.StringUtils;

public class SignatureParameters {

    private static final String SEP = "&";

    private final String method;
    private final String path;
    private final String payload;
    private final AuthorizationParameters authParams;

    public SignatureParameters(final String method, final String path, final String payload, final AuthorizationParameters authParams) {
        this.method = method;
        this.path = path;
        this.payload = payload;
        this.authParams = authParams;
    }

    public String constructSignatureBaseString() {
        StringBuilder builder = new StringBuilder();
        builder.append(method).append(SEP);
        builder.append(path).append(SEP);
        builder.append(authParams.constructSignatureString());
        if (StringUtils.isNotBlank(payload)) {
            builder.append(SEP).append(payload);
        }
        return builder.toString();
    }

    public AuthorizationParameters getAuthParams() {
        return authParams;
    }
}