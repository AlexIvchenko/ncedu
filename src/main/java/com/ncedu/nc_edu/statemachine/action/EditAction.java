package com.ncedu.nc_edu.statemachine.action;

import com.ncedu.nc_edu.statemachine.RecipeEvent;
import com.ncedu.nc_edu.statemachine.RecipeState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class EditAction implements Action<RecipeState, RecipeEvent> {
    @Override
    public void execute(StateContext<RecipeState, RecipeEvent> stateContext) {

    }
}
