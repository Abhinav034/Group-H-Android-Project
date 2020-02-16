package com.example.grouphfinalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.net.Inet4Address;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String CATEGORY_KEY = "Selected Category";
    public static final String CATEGORY_LIST = "List_of_Catagories";
    SwipeMenuListView listView;
    ImageButton button;
    String category;

    DatabaseHelper databaseHelper;
    public static SharedPreferences sharedPreferences;

    public static  ArrayList<String> catList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = this.getSharedPreferences("com.example.grouphfinalproject",Context.MODE_PRIVATE);


        listView = findViewById(R.id.listView);
        button = findViewById(R.id.addButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textAlert();
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

                if (index == 0){


                    //DataBase delete code

                    databaseHelper.removeNote(DatabaseHelper.COLUMN_CATEGORY, catList.get(position));
                    catList.remove(position);

                    arrayAdapter.notifyDataSetChanged();


                }
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

    private void textAlert(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this );
        alertDialog.setTitle("Add a category");
        alertDialog.setMessage("Enter a category");

        final EditText input= new EditText(MainActivity.this);

        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                category = input.getText().toString();
                catList.add(category);
                System.out.println("first");
                System.out.println(catList);

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







}
