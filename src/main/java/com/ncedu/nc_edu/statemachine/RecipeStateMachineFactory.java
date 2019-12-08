package com.ncedu.nc_edu.statemachine;

import com.ncedu.nc_edu.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class RecipeStateMachineFactory {
    private final StateMachineFactory<RecipeState, RecipeEvent> factory;

    @Autowired
    public RecipeStateMachineFactory(StateMachineFactory<RecipeState, RecipeEvent> factory) {
        this.factory = factory;
    }

    /**
     * Creates new statemachine with default initial state
     * @return a new statemachine instance
     */
    public StateMachine<RecipeState, RecipeEvent> getStateMachine() {
        StateMachine<RecipeState, RecipeEvent> machine = factory.getStateMachine();
        machine.start();
        return machine;
    }


    /**
     *  Creates a statemachine with recipe's state
     * @param recipe a recipe which state will be the initial state of the machine
     * @return a new statemachine instance
     */
    public StateMachine<RecipeState, RecipeEvent> getStateMachine(Recipe recipe) {
        if (recipe.getState() == null) {
            return this.getStateMachine();
        }

        RecipeState state = RecipeState.valueOf(recipe.getState());

        StateMachine<RecipeState, RecipeEvent> machine = factory.getStateMachine();
        machine.getStateMachineAccessor().doWithAllRegions(access -> {
            access.resetStateMachine(new DefaultStateMachineContext<>(state,
                    null, null, null, null));
        });
        machine.start();

        return machine;
    }

    /**
     * Creates a statemachine with given initial state
     * @param initialState a state to initialize statemachine
     * @return a new statemachine instance
     */
    public StateMachine<RecipeState, RecipeEvent> getStateMachine(RecipeState initialState) {
        StateMachine<RecipeState, RecipeEvent> machine = factory.getStateMachine();
        machine.getStateMachineAccessor().doWithAllRegions(access -> {
            access.resetStateMachine(new DefaultStateMachineContext<>(initialState,
                    null, null, null, null));
        });
        machine.start();
        return machine;
    }
}
