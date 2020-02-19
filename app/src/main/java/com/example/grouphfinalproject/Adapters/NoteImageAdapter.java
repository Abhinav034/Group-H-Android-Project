package com.example.grouphfinalproject.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.grouphfinalproject.R;

import java.io.File;
import java.util.ArrayList;

public class NoteImageAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> imagesPathList;

    public NoteImageAdapter(Context context, ArrayList<String> imagesPathList) {
        this.context = context;
        this.imagesPathList = imagesPathList;
    }

    @Override
    public int getCount() {
        return imagesPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_note_image, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imageview);

        Log.i("imageAdapter", "getView: " + imagesPathList.get(position));
        File imgFile = new File(imagesPathList.get(position));
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);

        }

        return convertView;
    }
}
