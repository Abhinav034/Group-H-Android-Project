package com.example.grouphfinalproject.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.io.IOException;

public class AudioFragment extends Fragment {
    private static final int AUDIO_REQUEST_CODE = 100;
    private MediaRecorder myRecorder;
    String path;
    private NoteModel noteModel;

    public AudioFragment(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ImageView ivRecord, ivStop;
        ivRecord = view.findViewById(R.id.btn_record);
        ivStop = view.findViewById(R.id.btn_stop);
        LinearLayout llAudio = view.findViewById(R.id.ll_audio);
        TextView tvAudio = view.findViewById(R.id.txt_audio);
        final Chronometer chronometer = view.findViewById(R.id.chronometer);

        if(!checkAudioPermission())
            requestAudioPermission();

        if(noteModel != null){
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + noteModel.getId() + ".3gp";

        } else{
            llAudio.setVisibility(View.GONE);
            tvAudio.setVisibility(View.VISIBLE);
        }


        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAudioPermission()){
                    setMediaRecorder();
                    try{
                        myRecorder.prepare();
                        myRecorder.start();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    chronometer.start();

                    ivRecord.setVisibility(View.GONE);
                    ivStop.setVisibility(View.VISIBLE);

                } else{
                    requestAudioPermission();
                }
            }

        });

        ivStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecorder.stop();
                myRecorder.release();
                myRecorder = null;

                chronometer.stop();
                ivRecord.setVisibility(View.VISIBLE);
                ivStop.setVisibility(View.GONE);

            }
        });

    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, AUDIO_REQUEST_CODE);
    }

    private boolean checkAudioPermission() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AUDIO_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }

    private void setMediaRecorder() {
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(path);
    }

}
