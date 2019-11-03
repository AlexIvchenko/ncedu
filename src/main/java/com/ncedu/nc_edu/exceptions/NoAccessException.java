package com.ncedu.nc_edu.exceptions;

public class NoAccessException extends Exception {
    public NoAccessException() {
        super("Access restricted");
    }
}
