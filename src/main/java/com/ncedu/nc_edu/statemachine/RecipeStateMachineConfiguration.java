package com.ncedu.nc_edu.statemachine;

import com.ncedu.nc_edu.statemachine.action.*;
import com.ncedu.nc_edu.statemachine.guard.IsModeratorGuard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

import static com.ncedu.nc_edu.statemachine.RecipeEvent.*;
import static com.ncedu.nc_edu.statemachine.RecipeState.*;

@Configuration
@EnableStateMachineFactory
public class RecipeStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<RecipeState, RecipeEvent> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<RecipeState, RecipeEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(false)
                .listener(new RecipeStateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<RecipeState, RecipeEvent> states) throws Exception {
        states
                .withStates()
                .initial(WAITING_FOR_APPROVAL)
                .end(RecipeState.DELETED)
                .states(EnumSet.allOf(RecipeState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<RecipeState, RecipeEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(WAITING_FOR_APPROVAL)
                .target(PUBLISHED)
                .event(APPROVE)
                .action(approveAction())

                .and()
                .withExternal()
                .source(WAITING_FOR_APPROVAL)
                .target(CHANGES_NEEDED)
                .event(REQUEST_FOR_CHANGES)
                .action(requestChangesAction())

                .and()
                .withExternal()
                .source(WAITING_FOR_APPROVAL)
                .target(DELETED)
                .event(DELETE)
                .action(deleteAction())

                .and()
                .withExternal()
                .source(CHANGES_NEEDED)
                .target(DELETED)
                .event(DELETE)
                .action(deleteAction())

                .and()
                .withExternal()
                .source(CHANGES_NEEDED)
                .target(WAITING_FOR_APPROVAL)
                .event(MAKE_CHANGES)
                .action(makeChangesAction())

                .and()
                .withExternal()
                .source(PUBLISHED)
                .target(EDITED)
                .event(EDIT)
                .action(editAction())

                .and()
                .withExternal()
                .source(EDITED)
                .target(PUBLISHED)
                .event(EDIT_APPROVE)
                .action(editApproveAction())

                .and()
                .withExternal()
                .source(EDITED)
                .target(PUBLISHED)
                .event(EDIT_CLONE)
                .action(editCloneAction())

                .and()
                .withExternal()
                .source(EDITED)
                .target(PUBLISHED)
                .event(EDIT_DECLINE)
                .action(editDeclineAction())

                .and()
                .withExternal()
                .source(PUBLISHED)
                .target(DELETED)
                .guard(isModeratorGuard())
                .action(deleteAction());
    }

    @Bean
    public ApproveAction approveAction() {
        return new ApproveAction();
    }

    @Bean
    public DeleteAction deleteAction() {
        return new DeleteAction();
    }

    @Bean
    public DeletionDeclineAction deletionDeclineAction() {
        return new DeletionDeclineAction();
    }

    @Bean
    public DeletionRequestAction deletionRequestAction() {
        return new DeletionRequestAction();
    }

    @Bean
    public EditAction editAction() {
        return new EditAction();
    }

    @Bean
    public EditApproveAction editApproveAction() {
        return new EditApproveAction();
    }

    @Bean
    public EditCloneAction editCloneAction() {
        return new EditCloneAction();
    }

    @Bean
    public EditDeclineAction editDeclineAction() {
        return new EditDeclineAction();
    }

    @Bean
    public ErrorAction errorAction() {
        return new ErrorAction();
    }

    @Bean
    public RequestChangesAction requestChangesAction() {
        return new RequestChangesAction();
    }

    @Bean
    public MakeChangesAction makeChangesAction() {
        return new MakeChangesAction();
    }

    @Bean
    public IsModeratorGuard isModeratorGuard() {
        return new IsModeratorGuard();
    }
}
