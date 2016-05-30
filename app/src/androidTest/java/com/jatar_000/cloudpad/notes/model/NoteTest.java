package com.jatar_000.cloudpad.notes.model;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 * Tests the Note Model
 * Created by Angel on 27.05.16.
 */
public class NoteTest extends TestCase {

    public void testMarkDownStrip() {
        Note note = new Note(0, Calendar.getInstance(), "#Title", "");
        assertTrue("Title".equals(note.getTitle()));
        note.setTitle("* List");
        assertTrue("List".equals(note.getTitle()));
    }
}
