package com.github.fdesu.data.validation;

public class BadResponse {

    private String errorMessage;

    public BadResponse() {
    }


    public BadResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
