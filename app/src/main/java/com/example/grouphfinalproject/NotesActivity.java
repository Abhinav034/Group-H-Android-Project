package com.example.grouphfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    public static final String SELECTED_NOTE = "selectedNote" ;
    ImageButton imageButton, mapButton;
    ListView listView;
    ArrayList<Note> Notes;
    String categoryName;

    private ArrayAdapter arrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        imageButton = findViewById(R.id.addNotes);
        mapButton = findViewById(R.id.maps);     // just for testing
        listView = findViewById(R.id.notesList);
        Notes = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make third activity
                Intent i = new Intent(NotesActivity.this, NoteDetails.class);
                i.putExtra(MainActivity.CATEGORY_KEY,categoryName);
                startActivity(i);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this , MapsActivity.class);
                startActivity(intent);
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(NotesActivity.this, NoteDetails.class);
                i.putExtra(SELECTED_NOTE, Notes.get(position));
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


        Notes.clear();
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(MainActivity.CATEGORY_KEY);

        Cursor cursor = databaseHelper.getAllNotes(categoryName);




        if (cursor.moveToFirst()) {
            do {

                Notes.add(new Note(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6)

                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // this part has to be replaced by custom adaptor
        String[] titleArray = new String[Notes.size()];

        for(int i = 0; i < Notes.size(); i++){

            titleArray[i] = Notes.get(i).getTitle();
        }


        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 ,titleArray );
        listView.setAdapter(arrayAdapter);

    }
}
