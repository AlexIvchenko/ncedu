package com.ncedu.nc_edu.exceptions;

public class UserDoesNotExistsException extends Exception {
    public UserDoesNotExistsException() {
        super("User with given id does not exist");
    }
}