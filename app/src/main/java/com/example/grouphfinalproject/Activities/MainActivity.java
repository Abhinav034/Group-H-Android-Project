package com.example.grouphfinalproject.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.grouphfinalproject.DatabaseHandlers.DatabaseHelper;
import com.example.grouphfinalproject.DatabaseHandlers.ObjectSerializer;
import com.example.grouphfinalproject.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String CATEGORY_KEY = "Selected Category";
    public static final String CATEGORY_LIST = "List_of_Catagories";
    SwipeMenuListView listView;
    ImageButton btnAddCategory;
    String category;

    DatabaseHelper databaseHelper;
    public static SharedPreferences sharedPreferences;

    public static  ArrayList<String> catList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Categories");




        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = this.getSharedPreferences("com.example.grouphfinalproject",Context.MODE_PRIVATE);


        listView = findViewById(R.id.listView);
        btnAddCategory = findViewById(R.id.btn_add_category);


        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCategoryAlert();
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this , NotesActivity.class);
                intent.putExtra(CATEGORY_KEY, catList.get(position));
                startActivity(intent);


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



                Cursor cursor = databaseHelper.getAllNotes(catList.get(position));


                if (cursor.moveToFirst()) {
                    do {


                        // files getting deleted
                        int idToDelete = cursor.getInt(0);
                        deleteAudioandImages(idToDelete);



                    } while (cursor.moveToNext());
                    cursor.close();
                }



                        databaseHelper.removeNote(DatabaseHelper.COLUMN_CATEGORY, catList.get(position));


                            // add code to delete media file
                            catList.remove(position);




                            try {
                                sharedPreferences.edit().putString(CATEGORY_LIST, ObjectSerializer.serialize(catList)).apply();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            reloadCategoryList();


                            Toast.makeText(MainActivity.this , "Category Deleted!!" , Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


    }


    @Override
    protected void onStart() {
        super.onStart();

        System.out.println(catList);
        reloadCategoryList();
    }

    private void reloadCategoryList() {
        catList.clear();
        System.out.println(catList);
        try {
            catList = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString(CATEGORY_LIST, ObjectSerializer.serialize(new ArrayList<>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(catList);
        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , catList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private void addCategoryAlert(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this );
        alertDialog.setTitle("Add a category");
        alertDialog.setMessage("Enter a category");

        final EditText input= new EditText(MainActivity.this);

        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                category = input.getText().toString();

                if (!catList.contains(category.toUpperCase())){

                    catList.add(category.toUpperCase());
                }
                else {
                    Toast.makeText(MainActivity.this,  category.toUpperCase() +" Already Exists !", Toast.LENGTH_SHORT).show();

                }



                try {
                    sharedPreferences.edit().putString(CATEGORY_LIST, ObjectSerializer.serialize(catList)).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                reloadCategoryList();
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
