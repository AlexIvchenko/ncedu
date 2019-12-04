package com.ncedu.nc_edu;

import com.ncedu.nc_edu.statemachine.RecipeEvent;
import com.ncedu.nc_edu.statemachine.RecipeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static com.ncedu.nc_edu.statemachine.RecipeEvent.*;
import static com.ncedu.nc_edu.statemachine.RecipeState.*;

@SpringBootTest
public class StateMachineTest {
    @Autowired
    private StateMachineFactory<RecipeState, RecipeEvent> factory;

    @Test
    public void contextLoads() {}

    @Test
    public void testFullCycle() throws Exception {
        StateMachine<RecipeState, RecipeEvent> machine = factory.getStateMachine();
        StateMachineTestPlan<RecipeState, RecipeEvent> plan =
                StateMachineTestPlanBuilder.<RecipeState, RecipeEvent>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(machine)
                        .step()
                        .expectState(CREATED)
                        .and()
                        .step()
                        .sendEvent(REQUEST_FOR_CHANGES)
                        .expectState(CHANGES_NEEDED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(MAKE_CHANGES)
                        .expectState(CREATED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(APPROVE)
                        .expectState(PUBLISHED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(EDIT)
                        .expectStateChanged(1)
                        .expectState(EDITED)
                        .and()
                        .step()
                        .sendEvent(EDIT_APPROVE)
                        .expectState(PUBLISHED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(DELETION_REQUEST)
                        .expectStateChanged(1)
                        .expectState(PENDING_FOR_DELETION)
                        .and()
                        .step()
                        .sendEvent(DELETION_APPROVE)
                        .expectState(DELETED)
                        .expectStateChanged(1)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void testInvalidTransition()  throws Exception {
        StateMachine<RecipeState, RecipeEvent> machine = factory.getStateMachine();
        StateMachineTestPlan<RecipeState, RecipeEvent> plan =
                StateMachineTestPlanBuilder.<RecipeState, RecipeEvent>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(machine)
                        .step()
                        .expectState(CREATED)
                        .and()
                        .step()
                        .sendEvent(REQUEST_FOR_CHANGES)
                        .expectState(CHANGES_NEEDED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(APPROVE)
                        .expectState(CHANGES_NEEDED)
                        .expectStateChanged(0)
                        .and()
                        .build();
        plan.test();
    }
}
