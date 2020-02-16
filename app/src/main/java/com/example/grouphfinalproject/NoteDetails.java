package com.example.grouphfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteDetails extends AppCompatActivity {


    EditText title_ET, description_ET, catagory_ET;
    //Button save_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_note);

        title_ET = findViewById(R.id.titleID);
        description_ET = findViewById(R.id.descriptionID);
        catagory_ET = findViewById(R.id.catagoryID);

        //save_BTN = findViewById(R.id.saveBTN);



    }

    public void saveNotes(View view) {

        // add to database.

        Toast.makeText(this, "Note Saved !!", Toast.LENGTH_SHORT).show();

    }
}
