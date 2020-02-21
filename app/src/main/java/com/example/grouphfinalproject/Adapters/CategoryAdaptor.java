package com.example.grouphfinalproject.Adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.grouphfinalproject.DatabaseHandlers.DatabaseHelper;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class CategoryAdaptor extends ArrayAdapter {

    Context context;
    int resource;
    List<String> cat_list;
    DatabaseHelper db;


    public CategoryAdaptor(@NonNull Context context, int resource , List<String> cat_list , DatabaseHelper db ) {
        super(context, resource, cat_list);
        this.context = context;
        this.resource = resource;
        this.cat_list = cat_list;
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view =  layoutInflater.inflate(resource , null);

        TextView cat = view.findViewById(R.id.cat_name);
        TextView size = view.findViewById(R.id.no_of_notes);


        cat.setText(cat_list.get(position));

        size.setText("( " +db.getAllNotes(cat_list.get(position)).getCount() + " )");



        return view;
    }


}
