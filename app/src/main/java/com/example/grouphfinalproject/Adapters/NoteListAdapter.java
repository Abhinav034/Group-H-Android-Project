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


public class NoteListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    List<NoteModel>noteModel;
    DatabaseHelper db;


    public NoteListAdapter(@NonNull Context context, int resource , List<NoteModel> noteModel , DatabaseHelper db ) {
        super(context, resource, noteModel);
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
        ImageView imageView = view.findViewById(R.id.imageView2);

        NoteModel currentNoteData = noteModel.get(position);


        titleTxt.setText(currentNoteData.getTitle());
        dateTxt.setText(currentNoteData.getCreatedTimeStamp());
        addressTxt.setText(getAddress(new LatLng(currentNoteData.getLatitude(), currentNoteData.getLongitude())));



        return view;
    }


    public String getAddress(LatLng latLng) {
        //get address
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList != null && addressList.size() > 0) {

                if (addressList.get(0).getFeatureName() != null) {
                    address += addressList.get(0).getFeatureName() + ", ";
                }
                if ((addressList.get(0).getThoroughfare() != null) && (addressList.get(0).getThoroughfare() != addressList.get(0).getFeatureName())) {
                    address += addressList.get(0).getThoroughfare() + ", ";
                }
                if (addressList.get(0).getLocality() != null) {
                    address += addressList.get(0).getLocality();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

}
