package com.example.InventoryManager.model;

public class ErrorMessage {
    private String errorInfo;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String errorInfo, String message) {
        this.errorInfo = errorInfo;
        this.message = message;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorInfo='" + errorInfo + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
