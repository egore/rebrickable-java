package com.rebrickable.lego.exceptions;

import com.rebrickable.exceptions.RebrickableException;

public class RequestThrottledException extends RebrickableException {

    public RequestThrottledException(String message) {
        super(message);
    }

}
