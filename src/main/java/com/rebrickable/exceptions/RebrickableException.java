package com.rebrickable.exceptions;

public class RebrickableException extends RuntimeException {

    public RebrickableException(String message) {
        super(message);
    }

    public RebrickableException(String message, Throwable cause) {
        super(message, cause);
    }

}
