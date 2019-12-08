package com.ncedu.nc_edu.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

@Slf4j
public class RecipeStateMachineListener implements StateMachineListener<RecipeState, RecipeEvent> {
    @Override
    public void stateChanged(State<RecipeState, RecipeEvent> oldState, State<RecipeState, RecipeEvent> newState) {
        if (oldState == null) {
            log.debug("State changed: -> " + newState.toString());
            return;
        }

        log.debug("State changed: " + oldState.toString() + " -> " + newState.toString());
    }

    @Override
    public void stateEntered(State<RecipeState, RecipeEvent> state) {
        log.debug("State entered: " + state.toString());
    }

    @Override
    public void stateExited(State<RecipeState, RecipeEvent> state) {
        log.debug("State exited: " + state.toString());
    }

    @Override
    public void eventNotAccepted(Message<RecipeEvent> message) {
        log.debug("Event not accepted: " + message);
    }

    @Override
    public void transition(Transition<RecipeState, RecipeEvent> transition) {

    }

    @Override
    public void transitionStarted(Transition<RecipeState, RecipeEvent> transition) {

    }

    @Override
    public void transitionEnded(Transition<RecipeState, RecipeEvent> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<RecipeState, RecipeEvent> stateMachine) {
        log.debug("Machine started");
    }

    @Override
    public void stateMachineStopped(StateMachine<RecipeState, RecipeEvent> stateMachine) {
        log.debug("Machine stopped");
    }

    @Override
    public void stateMachineError(StateMachine<RecipeState, RecipeEvent> stateMachine, Exception e) {

    }

    @Override
    public void extendedStateChanged(Object o, Object o1) {

    }

    @Override
    public void stateContext(StateContext<RecipeState, RecipeEvent> stateContext) {

    }
}
