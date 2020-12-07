 package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Items;
import model.Note;

public class MainActivity extends AppCompatActivity {

    private EditText dEtTitle;
    private Button dBtnAddItem;
    private Button dBtnAddNote;
    private LinearLayout dLlDynamicLayout;

    private int itemID = 0;
    private ArrayList<Items> noteItems;

    private DatabaseHelper dbHelper;

    private boolean isUpdate = false;
    private Note updatedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dEtTitle = findViewById(R.id.et_note_title);
        dBtnAddItem = findViewById(R.id.btn_add_item);
        dBtnAddNote = findViewById(R.id.btn_add_note);
        dLlDynamicLayout = findViewById(R.id.ll_dynamic_layout);

        dBtnAddNote.setEnabled(false);

        noteItems = new ArrayList<>();

        dbHelper = new DatabaseHelper(MainActivity.this);

        Bundle UserData = getIntent().getExtras();
        if(UserData != null){
            isUpdate = UserData.getBoolean("ISUPDATE");
             updatedNote = (Note) UserData.getSerializable("NOTE");

            if(isUpdate && updatedNote != null){
                ArrayList<Items> updatedItems = Items.covertJSONArrayStringToArrayList(updatedNote.noteItems);
                dEtTitle.setText(updatedNote.noteTitle);
                for(int i=0; i<updatedItems.size(); i++){
                    View dView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_entry, null);
                    final EditText dEtItem = dView.findViewById(R.id.et_item);
                    final ImageView dIvDone = dView.findViewById(R.id.iv_item_done);
                    dEtItem.setText(updatedItems.get(i).itemName);

                    dIvDone.setVisibility(View.INVISIBLE);
                    dLlDynamicLayout.addView(dView);
                    itemID = i + 1;
                    noteItems.add(updatedItems.get(i));
                }
            }
        }
    }

    public void onAddItemClicked(View view){
        dBtnAddItem.setEnabled(false);

        itemID++;

        View dView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_entry, null);
        final EditText dEtItem = dView.findViewById(R.id.et_item);
        final ImageView dIvDone = dView.findViewById(R.id.iv_item_done);

        dIvDone.setVisibility(View.INVISIBLE);

        dEtItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dIvDone.setVisibility(editable.length()>0 ? View.VISIBLE : View.GONE);
            }
        });

        dIvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dEtItem.setEnabled(false);
                dBtnAddItem.setEnabled(true);
                dBtnAddNote.setEnabled(true);

                Items newItem = new Items();
                newItem.id = itemID;
                newItem.itemName = dEtItem.getText().toString();
                newItem.isChecked = false;

                noteItems.add(newItem);
            }
        });

        dLlDynamicLayout.addView(dView);
    }

    public void onAddNoteClicked(View view){
        if(dEtTitle.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Title Can't be Empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(MainActivity.this, "Note Added Successfully", Toast.LENGTH_SHORT).show();

        Note newNote = new Note();
        newNote.noteTitle = dEtTitle.getText().toString();
        newNote.noteItems = Items.convertArrayListToJSONArrayString(noteItems);

        if(!isUpdate){
            dbHelper.insertDataToDatabase(dbHelper.getWritableDatabase(), newNote);
        }else {
            newNote.id = updatedNote.id;
            dbHelper.updatedItemsInDatabase(dbHelper.getWritableDatabase(), newNote);
        }
        setResult(Activity.RESULT_OK);
        finish();

//        JSONArray itemsArray = new JSONArray();

//        for(Items newVal : noteItems){
//            try {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put(Items.KEY_ID, newVal.id);
//                jsonObject.put(Items.KEY_ITEM_NAME, newVal.itemName);
//                jsonObject.put(Items.KEY_CHECKED, newVal.isChecked);
//
//                itemsArray.put(jsonObject);
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}