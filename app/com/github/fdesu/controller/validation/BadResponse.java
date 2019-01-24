package com.github.fdesu.controller.validation;

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
