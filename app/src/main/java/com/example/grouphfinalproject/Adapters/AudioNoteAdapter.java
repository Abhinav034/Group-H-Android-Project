package com.example.grouphfinalproject.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grouphfinalproject.R;

import java.io.IOException;
import java.util.ArrayList;

public class AudioNoteAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;
    MediaPlayer mediaPlayer;

    public AudioNoteAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_audio_notes, parent, false);

        final ImageView ivPlay = convertView.findViewById(R.id.iv_play);
        final ImageView ivStop = convertView.findViewById(R.id.iv_stop);
        TextView tvName = convertView.findViewById(R.id.tv_audio_name);

        tvName.setText(list.get(position).substring(list.get(position).lastIndexOf("/") + 1));
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(list.get(position));
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        ivPlay.setVisibility(View.VISIBLE);
                        ivStop.setVisibility(View.GONE);
                    }
                });

                mediaPlayer.start();
                ivPlay.setVisibility(View.GONE);
                ivStop.setVisibility(View.VISIBLE);
            }
        });


        ivStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                ivPlay.setVisibility(View.VISIBLE);
                ivStop.setVisibility(View.GONE);
            }
        });

        return convertView;

    }
}
