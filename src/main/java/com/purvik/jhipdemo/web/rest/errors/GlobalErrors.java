package com.purvik.jhipdemo.web.rest.errors;

public class GlobalErrors extends RuntimeException {

    private final String message;

    private final int applicationErrorCode;

    private final int httpStatus;

    public GlobalErrors(String message, int applicationErrorCode, int httpStatus) {
        this.message = message;
        this.applicationErrorCode = applicationErrorCode;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public int getApplicationErrorCode() {
        return applicationErrorCode;
    }
}
