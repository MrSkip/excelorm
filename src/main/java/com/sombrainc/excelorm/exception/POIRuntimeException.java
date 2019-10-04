package com.sombrainc.excelorm.exception;

public class POIRuntimeException extends RuntimeException {

    public POIRuntimeException(String message) {
        super(message);
    }

    public POIRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public POIRuntimeException(Throwable cause) {
        super(cause);
    }

}
