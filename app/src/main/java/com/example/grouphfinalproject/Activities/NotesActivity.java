package com.example.grouphfinalproject.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.grouphfinalproject.Adapters.NoteListAdapter;
import com.example.grouphfinalproject.DatabaseHandlers.DatabaseHelper;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.io.File;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    public static final String SELECTED_NOTE = "selectedNote" ;
    ImageButton btnAddNewNote;
    SwipeMenuListView listView;
    ArrayList<NoteModel> notesList, SearchList;
    String categoryName;
    ActionBar actionbar;
    Boolean isSearching = false;
    Spinner sort;


    //private ArrayAdapter arrayAdapter;
    NoteListAdapter adapter;
    DatabaseHelper databaseHelper;
    public static final String TAG = "Notes List";
    SearchView srch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_RIGHT_ICON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notes);
        actionbar = getSupportActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1792F2")));
        btnAddNewNote = findViewById(R.id.btn_add_notes);
        listView = findViewById(R.id.notesList);
        notesList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        sort = findViewById(R.id.sort_spinner);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){

                    switch (position){

                        case 1:
                            //sort by title
                            sortData(DatabaseHelper.COLUMN_TITLE);
                            break;
                        case 2:
                            // sort by date
                            sortData(DatabaseHelper.COLUMN_TIMESTAMP);

                            break;
                        default:
                            break;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        btnAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotesActivity.this, Main2Activity.class);
                i.putExtra(MainActivity.CATEGORY_KEY,categoryName);
                startActivity(i);

//                Intent intent = new Intent(NotesActivity.this, Main2Activity.class);
//                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(NotesActivity.this, Main2Activity.class);
                i.putExtra(SELECTED_NOTE, notesList.get(position));
                startActivity(i);

            }
        });


        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());

                delete.setIcon(R.drawable.ic_delete);
                delete.setBackground(new ColorDrawable(Color.parseColor("#F21717")));
                delete.setWidth(250);
                menu.addMenuItem(delete);
            }
        };
        listView.setMenuCreator(swipeMenuCreator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index){

                    case 0:

                        boolean removed = databaseHelper.removeNote(DatabaseHelper.COLUMN_ID, String.valueOf(notesList.get(position).getId()));
                        if (removed){


                            deleteAudioandImages(notesList.get(position).getId());
                            // add media file delete code here
                            loadListData();


                            Toast.makeText(NotesActivity.this , "Note Deleted!!" , Toast.LENGTH_SHORT).show();
                        }else{


                            Toast.makeText(NotesActivity.this , "Failed to delete Note!" , Toast.LENGTH_SHORT).show();

                        }

                        break;


                }





                return true;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


        srch = findViewById(R.id.searchBar);


        srch.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                SearchList = new ArrayList<>();
                if (!newText.isEmpty()){


                    for (NoteModel n:notesList
                    ) {

                        if (n.getTitle().contains(newText)){
                            SearchList.add(n);
                        }

                    }

                    adapter = new NoteListAdapter(NotesActivity.this , R.layout.notes_list_layout ,SearchList, databaseHelper );
                    listView.setAdapter(adapter);
                    isSearching = true;
                }else{

                    isSearching = false;
                    loadListData();


                }
                return false;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        isSearching = false;
        loadListData();


    }

    public void loadListData(){


        notesList.clear();
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(MainActivity.CATEGORY_KEY);


        actionbar.setTitle(categoryName + " : Notes");

        Cursor cursor = databaseHelper.getAllNotes(categoryName);

        if (cursor.moveToFirst()) {
            do {

                notesList.add(new NoteModel(
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
        
        adapter = new NoteListAdapter(this , R.layout.notes_list_layout , isSearching ? SearchList :notesList, databaseHelper );
        listView.setAdapter(adapter);

    }


    public void sortData(String colToSort){


        notesList.clear();
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(MainActivity.CATEGORY_KEY);


        actionbar.setTitle(categoryName + " : Notes");

        Cursor cursor = databaseHelper.getSortedNotes(colToSort, categoryName);

        if (cursor.moveToFirst()) {
            do {

                notesList.add(new NoteModel(
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

        adapter = new NoteListAdapter(this , R.layout.notes_list_layout , isSearching ? SearchList :notesList, databaseHelper );
        listView.setAdapter(adapter);

    }

    public void deleteAudioandImages(int noteID){
        File sdCard = Environment.getExternalStorageDirectory();
        File imageDirectory = new File(sdCard.getAbsolutePath() + "/Notes/Images/" + noteID);
            deleteRecursive(imageDirectory);

        File audioDirectory = new File(sdCard.getAbsolutePath() + "/Notes/Audio/" + noteID);
        deleteRecursive(audioDirectory);
    }


    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }
}
