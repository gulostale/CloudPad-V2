package com.jatar_000.cloudpad.notes.model;

/**
 * Helps to distinguish between different local change types for Server Synchronization.
 * Created by Jatar on 26.05.16.
 */
public enum DBStatus {

    VOID(""), LOCAL_CREATED("LOCAL_CREATED"), LOCAL_EDITED("LOCAL_EDITED"), LOCAL_DELETED("LOCAL_DELETED");

    private final String title;

    public String getTitle() {
        return title;
    }

    DBStatus(String title) {
        this.title = title;
    }
}
