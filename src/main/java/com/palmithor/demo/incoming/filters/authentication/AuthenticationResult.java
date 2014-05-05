package com.palmithor.demo.incoming.filters.authentication;


/**
 * Placeholder for results from Authenticator.
 * <p/>
 * Class has two constructors, one to use when an error occurs and another for success.
 */
public class AuthenticationResult {

    private final boolean isSuccess;
    private final String errorDetailKey;
    private final String errorDetailValue;

    public AuthenticationResult() {
        this.isSuccess = true;
        this.errorDetailKey = null;
        this.errorDetailValue = null;
    }

    public AuthenticationResult(final String errorDetailKey, final String errorDetailValue) {
        this.isSuccess = false;
        this.errorDetailKey = errorDetailKey;
        this.errorDetailValue = errorDetailValue;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorDetailKey() {
        return errorDetailKey;
    }

    public String getErrorDetailValue() {
        return errorDetailValue;
    }
}