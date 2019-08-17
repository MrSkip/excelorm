package com.sombrainc.excelorm.exception;

public class POIRuntimeException extends RuntimeException {
    public POIRuntimeException() {
    }

    public POIRuntimeException(String message) {
        super(message);
    }

    public POIRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public POIRuntimeException(Throwable cause) {
        super(cause);
    }

    public POIRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
