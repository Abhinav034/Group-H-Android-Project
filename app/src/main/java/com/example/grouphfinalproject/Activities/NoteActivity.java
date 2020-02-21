package com.example.grouphfinalproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.grouphfinalproject.Fragments.AudioFragment;
import com.example.grouphfinalproject.Fragments.MapFragment;
import com.example.grouphfinalproject.Fragments.NoteDetailsFragment;
import com.example.grouphfinalproject.Fragments.NoteImagesFragment;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.grouphfinalproject.Activities.MainActivity.CATEGORY_KEY;

public class NoteActivity extends AppCompatActivity {

    private ActionBar actionBar;
    public static NoteModel noteModelData;
    String catName;
    Intent intent;


    public static final int REQUEST_CODE = 1;
    public static final String TAG = "Main2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_RIGHT_ICON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_details);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1792F2")));

        intent = getIntent();
        noteModelData = (NoteModel) intent.getSerializableExtra(NotesListActivity.SELECTED_NOTE);



        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mItemSelectedListener);
        navigationView.setItemBackground(new ColorDrawable(Color.parseColor("#1792F2")));

        actionBar.setTitle("Note Details");



        if (noteModelData != null) {
            loadFragment(new NoteDetailsFragment(null));

        }else{
            catName = intent.getStringExtra(CATEGORY_KEY);
            loadFragment(new NoteDetailsFragment(catName));

        }
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_note:
                    actionBar.setTitle("Note");
                    if (noteModelData != null) {

                        loadFragment(new NoteDetailsFragment(null));

                    }else{
                        catName = intent.getStringExtra(CATEGORY_KEY);
                        loadFragment(new NoteDetailsFragment(catName));

                    }
                    return true;
                case R.id.navigation_images:
                    actionBar.setTitle("Images");
                    loadFragment(new NoteImagesFragment());
                    return true;
                case R.id.navigation_audio:
                    actionBar.setTitle("Audio");
//                    if(noteModelData != null){
                        loadFragment(new AudioFragment());
//                    } else {
//                        loadFragment(new AudioFragment(null));
//                    }
                    return true;
                case R.id.navigation_maps:
                    actionBar.setTitle("Map");
                        loadFragment(new MapFragment());

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

    }
}
