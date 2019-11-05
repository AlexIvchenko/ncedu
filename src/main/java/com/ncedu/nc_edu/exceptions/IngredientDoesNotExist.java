package com.ncedu.nc_edu.exceptions;

public class IngredientDoesNotExist extends Exception {
    public IngredientDoesNotExist(){
        super("Ingredient does not exist");
    }
}
