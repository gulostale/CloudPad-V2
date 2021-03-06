package com.jatar_000.cloudpad.notes.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jatar_000.cloudpad.notes.model.DBStatus;
import com.jatar_000.cloudpad.notes.model.Note;
import com.jatar_000.cloudpad.notes.util.NoteUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Helps to add, get, update and delete Notes with the option to trigger a Resync with the Server.
 * Created by Antonio on 26.05.16.
 */
public class NoteSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final int database_version = 4;
    private static final String database_name = "OWNCLOUD_NOTES";
    private static final String table_notes = "NOTES";
    private static final String key_id = "ID";
    private static final String key_status = "STATUS";
    private static final String key_title = "TITLE";
    private static final String key_modified = "MODIFIED";
    private static final String key_content = "CONTENT";
    private static final String[] columns = {key_id, key_status, key_title, key_modified, key_content};

    private NoteServerSyncHelper serverSyncHelper = null;
    private Context context = null;

    public NoteSQLiteOpenHelper(Context context) {
        super(context, database_name, null, database_version);
        this.context = context;
        serverSyncHelper = new NoteServerSyncHelper(this);
    }

    public NoteServerSyncHelper getNoteServerSyncHelper() {
        return serverSyncHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE '" + table_notes + "' ( '" +
                key_id + "' INTEGER PRIMARY KEY AUTOINCREMENT, '" +
                key_status + "' VARCHAR(50), '" +
                key_title + "' TEXT, '" +
                key_modified + "' TEXT, '" +
                key_content + "' TEXT)");
    }

     @SuppressWarnings("UnusedReturnValue")
    public long addNoteAndSync(String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(key_status, DBStatus.LOCAL_CREATED.getTitle());
        values.put(key_title, NoteUtil.generateNoteTitle(content));
        values.put(key_content, content);

        long id = db.insert(table_notes,
                null,
                values);
        db.close();
        serverSyncHelper.uploadNewNotes();
        return id;
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteSQLiteOpenHelper.key_id, note.getId());
        values.put(NoteSQLiteOpenHelper.key_status, DBStatus.VOID.getTitle());
        values.put(NoteSQLiteOpenHelper.key_title, note.getTitle());
        values.put(NoteSQLiteOpenHelper.key_modified, note.getModified(NoteSQLiteOpenHelper.DATE_FORMAT));
        values.put(NoteSQLiteOpenHelper.key_content, note.getContent());
        db.insert(NoteSQLiteOpenHelper.table_notes,
                null,
                values);
        db.close();
    }

    @SuppressWarnings("unused")
    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(table_notes,
                        columns,
                        key_id + " = ? AND " + key_status + " != ?",
                        new String[]{String.valueOf(id), DBStatus.LOCAL_DELETED.getTitle()},
                        null,
                        null,
                        null,
                        null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Calendar modified = Calendar.getInstance();
        try {
            String modifiedStr = cursor != null ? cursor.getString(3) : null;
            if (modifiedStr != null)
                modified.setTime(new SimpleDateFormat(DATE_FORMAT, Locale.GERMANY).parse(modifiedStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Note note = new Note(Long.valueOf(cursor != null ? cursor.getString(0) : null), modified, cursor != null ? cursor.getString(2) : null, cursor.getString(4));
        cursor.close();
        return note;
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_notes + " WHERE " + key_status + " != ? ORDER BY " + key_modified + " DESC", new String[]{DBStatus.LOCAL_DELETED.getTitle()});
        if (cursor.moveToFirst()) {
            do {
                Calendar modified = Calendar.getInstance();
                try {
                    String modifiedStr = cursor.getString(3);
                    if (modifiedStr != null)
                        modified.setTime(new SimpleDateFormat(DATE_FORMAT, Locale.GERMANY).parse(modifiedStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notes.add(new Note(Long.valueOf(cursor.getString(0)), modified, cursor.getString(2), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public List<Note> searchNotes(String query) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_notes + " WHERE " + key_status + " != ? AND " + key_content + " LIKE ? ORDER BY " + key_modified + " DESC", new String[]{DBStatus.LOCAL_DELETED.getTitle(), "%" + query + "%"});
        if (cursor.moveToFirst()) {
            do {
                Calendar modified = Calendar.getInstance();
                try {
                    String modifiedStr = cursor.getString(3);
                    if (modifiedStr != null)
                        modified.setTime(new SimpleDateFormat(DATE_FORMAT, Locale.GERMANY).parse(modifiedStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notes.add(new Note(Long.valueOf(cursor.getString(0)), modified, cursor.getString(2), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public List<Note> getNotesByStatus(DBStatus status) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_notes + " WHERE " + key_status + " = ?", new String[]{status.getTitle()});
        if (cursor.moveToFirst()) {
            do {
                Calendar modified = Calendar.getInstance();
                try {
                    String modifiedStr = cursor.getString(3);
                    if (modifiedStr != null)
                        modified.setTime(new SimpleDateFormat(DATE_FORMAT, Locale.GERMANY).parse(modifiedStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notes.add(new Note(Long.valueOf(cursor.getString(0)), modified, cursor.getString(2), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int updateNoteAndSync(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        DBStatus newStatus = DBStatus.LOCAL_EDITED;
        Cursor cursor =
                db.query(table_notes,
                        columns,
                        key_id + " = ? AND " + key_status + " != ?",
                        new String[]{String.valueOf(note.getId()), DBStatus.LOCAL_DELETED.getTitle()},
                        null,
                        null,
                        null,
                        null);
        if (cursor != null) {
            cursor.moveToFirst();
            String status = cursor.getString(1);
            if (!"".equals(status) && DBStatus.valueOf(status) == DBStatus.LOCAL_CREATED) {
                newStatus = DBStatus.LOCAL_CREATED;
            }
            cursor.close();
        }
        ContentValues values = new ContentValues();
        values.put(key_id, note.getId());
        values.put(key_status, newStatus.getTitle());
        values.put(key_title, note.getTitle());
        values.put(key_modified, note.getModified(DATE_FORMAT));
        values.put(key_content, note.getContent());
        int i = db.update(table_notes,
                values,
                key_id + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        serverSyncHelper.uploadEditedNotes();
        return i;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_id, note.getId());
        values.put(key_status, DBStatus.VOID.getTitle());
        values.put(key_title, note.getTitle());
        values.put(key_modified, note.getModified(DATE_FORMAT));
        values.put(key_content, note.getContent());
        int i = db.update(table_notes,
                values,
                key_id + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return i;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int deleteNoteAndSync(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_status, DBStatus.LOCAL_DELETED.getTitle());
        int i = db.update(table_notes,
                values,
                key_id + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        serverSyncHelper.uploadDeletedNotes();
        return i;
    }

    public void deleteNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_notes,
                key_id + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        clearDatabase();
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_notes, null, null);
        db.close();
    }

    public Context getContext() {
        return context;
    }

    public void synchronizeWithServer() {
        serverSyncHelper.synchronize();
    }
}
