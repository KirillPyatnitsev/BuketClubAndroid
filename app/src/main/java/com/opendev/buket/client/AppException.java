package com.opendev.buket.client;

/**
 * Created by leon on 15/07/16.
 */
public class AppException extends RuntimeException {

    public AppException() {
    }

    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public AppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }
}
