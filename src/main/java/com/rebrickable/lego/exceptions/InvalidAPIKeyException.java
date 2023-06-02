package com.rebrickable.lego.exceptions;

import com.rebrickable.exceptions.RebrickableException;

public class InvalidAPIKeyException extends RebrickableException {

    public InvalidAPIKeyException(String message) {
        super(message);
    }

}
