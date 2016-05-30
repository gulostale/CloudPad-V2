package com.jatar_000.cloudpad.notes.model;

/**
 * Created by Jatar on 26.05.16.
 */
public class SectionItem implements Item {
    String title = "";

    public SectionItem(String title) {
        this.title = title;
    }

    public String geTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
