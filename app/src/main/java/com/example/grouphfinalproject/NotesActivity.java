package com.example.grouphfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private static final String NOTE_ID = "selectedNoteId" ;
    ImageButton imageButton;
    ListView listView;
    ArrayList<Note> Notes;

    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        imageButton = findViewById(R.id.addNotes);
        listView = findViewById(R.id.notesList);
        Notes = new ArrayList<>();



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make third activity
                Intent i = new Intent(NotesActivity.this, NoteDetails.class);
                startActivity(i);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(NotesActivity.this, NoteDetails.class);
                i.putExtra(NOTE_ID,Notes.get(position).getId());
                startActivity(i);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshList();


    }

    public void refreshList(){

        // this part has to be replaced by custom adaptor
        String[] titleArray = new String[Notes.size()];

        for(int i = 0; i < Notes.size(); i++){

            titleArray[i] = Notes.get(i).getTitle();
        }


        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 ,titleArray );
        listView.setAdapter(arrayAdapter);

    }
}
