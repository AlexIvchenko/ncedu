package com.ncedu.nc_edu.statemachine;

public enum RecipeState {
    // UNLISTED:
    WAITING_FOR_APPROVAL,
    CHANGES_NEEDED,
    // PUBLIC:
    EDITED,
    PUBLISHED,
    // ACHIEVED:
    DELETED
}
