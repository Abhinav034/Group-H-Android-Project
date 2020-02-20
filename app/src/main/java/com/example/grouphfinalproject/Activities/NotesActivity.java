package com.example.grouphfinalproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.grouphfinalproject.DatabaseHandlers.DatabaseHelper;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    public static final String SELECTED_NOTE = "selectedNote" ;
    ImageButton btnAddNewNote;
    SwipeMenuListView listView;
    ArrayList<NoteModel> notesList;
    String categoryName;

    private ArrayAdapter arrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        btnAddNewNote = findViewById(R.id.btn_add_notes);
        listView = findViewById(R.id.notesList);
        notesList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);



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

                            // add media file delete code here
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




    }

    @Override
    protected void onStart() {
        super.onStart();

        loadListData();


    }

    public void loadListData(){


        notesList.clear();
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(MainActivity.CATEGORY_KEY);

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

        // this part has to be replaced by custom adaptor
        String[] titleArray = new String[notesList.size()];

        for(int i = 0; i < notesList.size(); i++){

            titleArray[i] = notesList.get(i).getTitle();
        }


        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 ,titleArray );
        listView.setAdapter(arrayAdapter);

    }
}
