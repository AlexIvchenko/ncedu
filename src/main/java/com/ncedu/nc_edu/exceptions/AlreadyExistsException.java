package com.ncedu.nc_edu.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {
    private final String entity;
    private final String field;

    /**
     * *Entity* with given *field* is already exists
     */
    public AlreadyExistsException(String entity, String field) {
        super(entity + " with given " + field + " is already exists");
        this.entity = entity;
        this.field = field;
    }
}