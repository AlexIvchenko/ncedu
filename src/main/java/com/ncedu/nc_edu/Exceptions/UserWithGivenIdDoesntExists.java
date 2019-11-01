package com.ncedu.nc_edu.Exceptions;

public class UserWithGivenIdDoesntExists extends Exception {
    public UserWithGivenIdDoesntExists() {
        super("User with given id does not exists");
    }
}