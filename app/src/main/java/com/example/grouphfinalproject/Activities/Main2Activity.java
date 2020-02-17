package com.example.grouphfinalproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.grouphfinalproject.Fragments.AudioFragment;
import com.example.grouphfinalproject.Fragments.NoteDetailsFragment;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.grouphfinalproject.Activities.MainActivity.CATEGORY_KEY;

public class Main2Activity extends AppCompatActivity {

    private ActionBar actionBar;
    NoteModel noteModelData;
    String catName;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        actionBar = getSupportActionBar();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mItemSelectedListener);

        actionBar.setTitle("Note Details");

        intent = getIntent();
        noteModelData = (NoteModel) intent.getSerializableExtra(NotesActivity.SELECTED_NOTE);

        if (noteModelData != null) {

            loadFragment(new NoteDetailsFragment(noteModelData));


        }else{
            catName = intent.getStringExtra(CATEGORY_KEY);
            loadFragment(new NoteDetailsFragment(catName));

        }



    }

    private BottomNavigationView.OnNavigationItemSelectedListener mItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()){
                case R.id.navigation_note:
                    actionBar.setTitle("Note");
                    if (noteModelData != null) {

                        loadFragment(new NoteDetailsFragment(noteModelData));

                    }else{
                        catName = intent.getStringExtra(CATEGORY_KEY);
                        loadFragment(new NoteDetailsFragment(catName));

                    }                    return true;
                case R.id.navigation_images:
                    actionBar.setTitle("Images");
                    return true;
                case R.id.navigation_audio:
                    actionBar.setTitle("Audio");
                    if(noteModelData != null){
                        loadFragment(new AudioFragment(noteModelData));
                    } else {
                        loadFragment(new AudioFragment(null));
                    }
                    return true;
                case R.id.navigation_maps:
                    actionBar.setTitle("Map");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }
}
