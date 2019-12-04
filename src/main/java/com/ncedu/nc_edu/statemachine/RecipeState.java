package com.ncedu.nc_edu.statemachine;

public enum RecipeState {
    // UNLISTED:
    CREATED,
    CHANGES_NEEDED,
    EDITED,
    // PUBLIC:
    PUBLISHED,
    PENDING_FOR_DELETION,
    // ACHIEVED
    DELETED
}
