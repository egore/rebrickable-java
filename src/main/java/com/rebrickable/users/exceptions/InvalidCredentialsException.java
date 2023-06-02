package com.rebrickable.users.exceptions;

import com.rebrickable.exceptions.RebrickableException;

public class InvalidCredentialsException extends RebrickableException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
