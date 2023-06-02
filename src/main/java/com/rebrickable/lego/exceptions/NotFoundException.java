package com.rebrickable.lego.exceptions;

import com.rebrickable.exceptions.RebrickableException;

public class NotFoundException extends RebrickableException {

    public NotFoundException(String message) {
        super(message);
    }

}
