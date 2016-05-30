package com.jatar_000.cloudpad.notes.util;

import in.uncod.android.bypass.Bypass;

/**
 * Provides basic functionality for Note operations.
 * Created by Jatar on 26.05.16.
 */
public class NoteUtil {
    private static final Bypass bypass = new Bypass();

    public static CharSequence parseMarkDown(String s) {
        StringBuilder sb = new StringBuilder();
        for (String line : s.split("\n")) {
            sb.append(line);
            // If line is not a list item
            if (!line.trim().matches("^([\\-*]|[0-9]+\\.)(.)*")) {
                sb.append("  ");
            }
            sb.append("\n");
        }
        return bypass.markdownToSpannable(sb.toString());
    }

    public static String removeMarkDown(String s) {
        return s == null ? "" : s.replaceAll("[#*-]", "").trim();
    }

    public static boolean isEmptyLine(String line) {
        return removeMarkDown(line).trim().length() == 0;
    }

    public static String generateNoteExcerpt(String content) {
        return getLineWithoutMarkDown(content, 1);
    }

    public static String generateNoteTitle(String content) {
        return getLineWithoutMarkDown(content, 0);
    }

    public static String getLineWithoutMarkDown(String content, int lineNumber) {
        String line = "";
        if (content.contains("\n")) {
            String[] lines = content.split("\n");
            int currentLine = lineNumber;
            while (currentLine < lines.length && NoteUtil.isEmptyLine(lines[currentLine])) {
                currentLine++;
            }
            if (currentLine < lines.length) {
                line = NoteUtil.removeMarkDown(lines[currentLine]);
            }
        } else {
            line = content;
        }
        return line;
    }
}
