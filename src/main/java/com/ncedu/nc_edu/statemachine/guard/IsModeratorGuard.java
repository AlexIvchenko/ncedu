package com.ncedu.nc_edu.statemachine.guard;

import com.ncedu.nc_edu.security.SecurityAccessResolver;
import com.ncedu.nc_edu.statemachine.RecipeEvent;
import com.ncedu.nc_edu.statemachine.RecipeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class IsModeratorGuard implements Guard<RecipeState, RecipeEvent> {
    @Autowired
    SecurityAccessResolver securityAccessResolver;

    @Override
    public boolean evaluate(StateContext<RecipeState, RecipeEvent> context) {
        return this.securityAccessResolver.isModerator();
    }
}
