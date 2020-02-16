package com.example.grouphfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetails extends AppCompatActivity {


    EditText title_ET, description_ET, catagory_ET;
    DatabaseHelper databaseHelper;

    Location CurrentLocation;
    Boolean noteExists = false;
    Note noteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_note);

        databaseHelper = new DatabaseHelper(this);

        title_ET = findViewById(R.id.titleID);
        description_ET = findViewById(R.id.descriptionID);
        catagory_ET = findViewById(R.id.catagoryID);

        //save_BTN = findViewById(R.id.saveBTN);

        Intent intent = getIntent();
        noteData = (Note) intent.getSerializableExtra(NotesActivity.SELECTED_NOTE);

        if (noteData != null) {

            noteExists = true;
            title_ET.setText(noteData.getTitle());
            description_ET.setText(noteData.getDescription());
            catagory_ET.setText(noteData.getCategory());

        }




    }

    public void saveNotes(View view) {

        // add to database.
        String title = title_ET.getText().toString();
        String description = description_ET.getText().toString();
        String category = catagory_ET.getText().toString();




        if (noteExists){

            Boolean updated = databaseHelper.updateNote(noteData.getId(), title, description, category);

            Toast.makeText(this, updated ? "Note Updated" : "Error in updating Note Data", Toast.LENGTH_SHORT).show();


        }else{

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");

            noteExists = databaseHelper.addNote(title, description, category,
                    format.format(new Date()),CurrentLocation.getLatitude(), CurrentLocation.getLongitude());


            Toast.makeText(this,noteExists ? "Note Saved !!" : "Error in saving Note Data !!", Toast.LENGTH_SHORT).show();

        }





        Toast.makeText(this, "Note Saved !!", Toast.LENGTH_SHORT).show();

    }
}
