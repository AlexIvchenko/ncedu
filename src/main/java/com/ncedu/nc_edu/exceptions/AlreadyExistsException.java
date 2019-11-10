package com.ncedu.nc_edu.exceptions;

public class AlreadyExistsException extends RuntimeException {
    /**
     * *Entity* with given *field* is already exists
     */
    public AlreadyExistsException(String entity, String field) {
        super(entity + " with given " + field + " is already exists");
    }
}