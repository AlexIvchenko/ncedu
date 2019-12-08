package com.ncedu.nc_edu.statemachine.guard;

import com.ncedu.nc_edu.services.RecipeService;
import com.ncedu.nc_edu.statemachine.RecipeEvent;
import com.ncedu.nc_edu.statemachine.RecipeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class IsModeratorGuard implements Guard<RecipeState, RecipeEvent> {
    @Autowired
    RecipeService recipeService;

    @Override
    public boolean evaluate(StateContext<RecipeState, RecipeEvent> context) {
        return false;
    }
}
