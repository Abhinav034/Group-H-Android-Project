package com.example.grouphfinalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
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

    SwipeMenuListView listView;
    ImageButton button;
    String category;

    ArrayList<String>catList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.addButton);

         arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , catList);
         listView.setAdapter(arrayAdapter);
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






                    catList.remove(position);

                    arrayAdapter.notifyDataSetChanged();


                }
                return true;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);




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
                arrayAdapter.notifyDataSetChanged();

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
