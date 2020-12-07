package com.example.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import model.Note;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String COL_ID = "id";
    private final static String COL_TITLE = "title";
    private final static String COL_ITEMS = "items";
    private final static String TABLE_NAME = "notes";

    private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT," + COL_ITEMS + " TEXT)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertDataToDatabase(SQLiteDatabase db, Note note){
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, note.noteTitle);
        cv.put(COL_ITEMS, note.noteItems);

        db.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<Note> getNotesFromDatabase(SQLiteDatabase db){
        ArrayList<Note> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if(cursor.moveToFirst())
            do {
                Note newNote = new Note();
                newNote.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                newNote.noteTitle = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                newNote.noteItems = cursor.getString(cursor.getColumnIndex(COL_ITEMS));
                items.add(newNote);
            }while (cursor.moveToNext());
        return items;
    }

    public void updatedItemsInDatabase(SQLiteDatabase db, Note note){
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, note.noteTitle);
        cv.put(COL_ITEMS, note.noteItems);
        db.update(TABLE_NAME, cv, COL_ID+"="+note.id, null);
    }

    public void deleteNote(Note note, SQLiteDatabase database){
        database.delete(TABLE_NAME, COL_ID+"="+note.id, null);
    }
}
