package com.opendev.buket.club.web.response;

public class AlphaPayResponse extends DefaultResponse {

    private String orderId;

    private String formUrl;

    private String errorCode;

    private String errorMessage;

    public AlphaPayResponse() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return orderId + " " + formUrl + " " + errorCode + " " + errorMessage;
    }
}
