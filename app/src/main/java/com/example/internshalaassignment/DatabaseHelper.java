package com.example.internshalaassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_CONTENT + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addNote(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, noteModel.getTitle());
        values.put(COLUMN_CONTENT, noteModel.getContent());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<NoteModel> getAllNotes() {
        List<NoteModel> noteModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                noteModel.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                noteModel.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                noteModels.add(noteModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return noteModels;
    }

    public NoteModel getNoteById(long noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(noteId)}, null, null, null);

        NoteModel noteModel = new NoteModel();
        if (cursor.moveToFirst()) {
            noteModel.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            noteModel.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            noteModel.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
        }

        cursor.close();
        db.close();
        return noteModel;
    }

    public int updateNote(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, noteModel.getTitle());
        values.put(COLUMN_CONTENT, noteModel.getContent());

        // Updating row
        int result = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(noteModel.getId())});

        db.close();
        return result;
    }

    public void deleteNote(long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(noteId)});
        db.close();
    }
}
