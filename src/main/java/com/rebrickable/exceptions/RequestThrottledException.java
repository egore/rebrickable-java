package com.rebrickable.exceptions;

public class RequestThrottledException extends RebrickableException {

    public RequestThrottledException(String message) {
        super(message);
    }

    public static class Response {
        public String detail;
    }

}
