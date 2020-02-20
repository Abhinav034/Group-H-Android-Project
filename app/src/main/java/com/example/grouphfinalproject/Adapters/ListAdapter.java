package com.example.grouphfinalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.grouphfinalproject.DatabaseHandlers.DatabaseHelper;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.util.List;


public class ListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    List<NoteModel>noteModel;
    DatabaseHelper db;





    public ListAdapter(@NonNull Context context, int resource , List<NoteModel> noteModel , DatabaseHelper db ) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.noteModel = noteModel;
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view =  layoutInflater.inflate(resource , null);

        TextView titleTxt = view.findViewById(R.id.titleText);
        TextView addressTxt = view.findViewById(R.id.addressText);
        TextView dateTxt = view.findViewById(R.id.dateText);

        NoteModel noteModel1 = noteModel.get(position);


        titleTxt.setText(noteModel1.getTitle());
        addressTxt.setText(noteModel1.getDescription());
        dateTxt.setText(noteModel1.getCreatedTimeStamp());


        return view;
    }
}
