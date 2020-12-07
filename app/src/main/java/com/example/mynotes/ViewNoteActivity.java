package com.example.mynotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import model.Items;
import model.Note;

public class ViewNoteActivity extends AppCompatActivity implements NoteListAdapter.NoteListClickListener {

    private RecyclerView drcNotes;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        drcNotes = findViewById(R.id.rc_notes);
        drcNotes.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new DatabaseHelper(ViewNoteActivity.this);

        setDataToRecycler();
    }

    public void onAddNewNoteClicked(View view){
        startActivityForResult(new Intent(ViewNoteActivity.this, MainActivity.class), 919);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && (requestCode == 919 || requestCode == 111)){
            setDataToRecycler();
        }
    }

    public void setDataToRecycler(){
        ArrayList<Note> notes = dbHelper.getNotesFromDatabase(dbHelper.getReadableDatabase());

        NoteListAdapter adapter = new NoteListAdapter(ViewNoteActivity.this, notes);
        adapter.setListener(ViewNoteActivity.this);
        drcNotes.setAdapter(adapter);
    }

    @Override
    public void onItemUpdateClicked(Items updatedItem, Note note, boolean checkedValue) {
        ArrayList<Items> oldItemsValues = Items.covertJSONArrayStringToArrayList(note.noteItems);

        if(oldItemsValues != null && oldItemsValues.size() > 0){
            for(Items oldItem : oldItemsValues){
                if(oldItem.id == updatedItem.id){
                    oldItem.isChecked = checkedValue;
                }
            }
        }
        Note updatedNote = new Note();
        updatedNote.id = note.id;
        updatedNote.noteTitle = note.noteTitle;
        updatedNote.noteItems = Items.convertArrayListToJSONArrayString(oldItemsValues);

        dbHelper.updatedItemsInDatabase(dbHelper.getWritableDatabase(), updatedNote);
        setDataToRecycler();
    }

    @Override
    public void onEditNoteClicked(Note note) {
        startActivityForResult(new Intent(ViewNoteActivity.this, MainActivity.class).putExtra("NOTE", note).putExtra("ISUPDATE", true), 111);
    }

    @Override
    public void onDeleteNoteClicked(Note note) {
        dbHelper.deleteNote(note, dbHelper.getWritableDatabase());
        setDataToRecycler();
        Toast.makeText(ViewNoteActivity.this, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
    }
}