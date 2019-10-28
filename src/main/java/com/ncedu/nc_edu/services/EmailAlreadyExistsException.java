package com.ncedu.nc_edu.services;

public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException() {
        super("User with given email is already registered");
    }
}
