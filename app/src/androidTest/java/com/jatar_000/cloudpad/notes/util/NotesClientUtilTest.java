package com.jatar_000.cloudpad.notes.util;

import junit.framework.TestCase;

/**
 * Tests the NotesClientUtil
 * Created by Angel on 27.05.16.
 */
public class NotesClientUtilTest extends TestCase {
    public void testIsHttp() {
        assertTrue(NotesClientUtil.isHttp("http://example.com"));
        assertTrue(NotesClientUtil.isHttp("http://www.example.com/"));
        assertFalse(NotesClientUtil.isHttp("https://www.example.com/"));
        assertFalse(NotesClientUtil.isHttp(null));
    }

    public void testIsValidURLTest() {
        assertTrue(NotesClientUtil.isValidURL("https://demo.cloudpad.org/"));
        assertFalse(NotesClientUtil.isValidURL("https://www.example.com/"));
        assertFalse(NotesClientUtil.isValidURL("htp://www.example.com/"));
        assertFalse(NotesClientUtil.isValidURL(null));
    }
}