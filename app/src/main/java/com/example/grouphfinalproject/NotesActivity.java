package com.example.grouphfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class NotesActivity extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        imageButton = findViewById(R.id.addNotes);
        listView = findViewById(R.id.notesList);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });


    }


}
