package com.jatar_000.cloudpad.notes.model;

import com.jatar_000.cloudpad.notes.persistence.NoteSQLiteOpenHelper;
import com.jatar_000.cloudpad.notes.util.NoteUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressWarnings("serial")
public class Note implements Item, Serializable {
    private long id = 0;
    private String title = "";
    private Calendar modified = null;
    private String content = "";
    private String excerpt = "";

    public Note(long id, Calendar modified, String title, String content) {
        this.id = id;
        if (title != null)
            setTitle(title);
        setTitle(title);
        setContent(content);
        this.modified = modified;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = NoteUtil.removeMarkDown(title);
    }

    @SuppressWarnings("WeakerAccess")
    public Calendar getModified() {
        return modified;
    }

    public String getModified(String format) {
        return new SimpleDateFormat(format, Locale.GERMANY)
                .format(this.getModified().getTimeInMillis());
    }

    public void setModified(Calendar modified) {
        this.modified = modified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        setExcerpt(content);
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    private void setExcerpt(String content) {
        excerpt = NoteUtil.generateNoteExcerpt(content);
    }

    public CharSequence getSpannableContent() {
            return NoteUtil.parseMarkDown(getContent());
    }

    @Override
    public String toString() {
        return "#" + getId() + " " + getTitle() + " (" + getModified(NoteSQLiteOpenHelper.DATE_FORMAT) + ")";
    }

    @Override
    public boolean isSection() {
        return false;
    }
}