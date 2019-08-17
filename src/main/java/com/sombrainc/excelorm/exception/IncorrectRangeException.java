package com.sombrainc.excelorm.exception;

public class IncorrectRangeException extends POIRuntimeException {

    public IncorrectRangeException(String message) {
        super(message);
    }

    public IncorrectRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
