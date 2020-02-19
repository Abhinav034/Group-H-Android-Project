package com.example.grouphfinalproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.grouphfinalproject.Fragments.AudioFragment;
import com.example.grouphfinalproject.Fragments.NoteDetailsFragment;
import com.example.grouphfinalproject.Fragments.NoteImagesFragment;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.grouphfinalproject.Activities.MainActivity.CATEGORY_KEY;

public class Main2Activity extends AppCompatActivity {

    private ActionBar actionBar;
    NoteModel noteModelData;
    String catName;
    Intent intent;

    NoteDetailsFragment frag;

    public static final int REQUEST_CODE = 1;
    public static final String TAG = "Main2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        intent = getIntent();
        noteModelData = (NoteModel) intent.getSerializableExtra(NotesActivity.SELECTED_NOTE);

        actionBar = getSupportActionBar();
        frag =   new NoteDetailsFragment(noteModelData);

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mItemSelectedListener);

        actionBar.setTitle("Note Details");



        if (noteModelData != null) {
            loadFragment(frag);

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

                        loadFragment(frag);

                    }else{
                        catName = intent.getStringExtra(CATEGORY_KEY);
                        loadFragment(new NoteDetailsFragment(catName));

                    }
                    return true;
                case R.id.navigation_images:
                    actionBar.setTitle("Images");
                    loadFragment(new NoteImagesFragment(noteModelData));
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == REQUEST_CODE) {
//            Log.i(TAG, "onRequestPermissionsResult: " + REQUEST_CODE);
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "onRequestPermissionsResult: IF ");
//                frag.setData();
//
//            }else{
//                Log.i(TAG, "onRequestPermissionsResult: ELSE");
//                Toast.makeText(this, "Requires permission to access location.", Toast.LENGTH_SHORT).show();
//            }
//        }

    }
}
