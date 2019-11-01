package com.ncedu.nc_edu.exceptions;

public class UserWithGivenIdDoesntExist extends Exception {
    public UserWithGivenIdDoesntExist() {
        super("User with given id does not exist");
    }
}