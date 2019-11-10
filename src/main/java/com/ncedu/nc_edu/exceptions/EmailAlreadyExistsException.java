package com.ncedu.nc_edu.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("User with given email is already registered");
    }
}