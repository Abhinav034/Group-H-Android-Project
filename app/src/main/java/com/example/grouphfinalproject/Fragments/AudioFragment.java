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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioFragment extends Fragment {
    private static final int AUDIO_REQUEST_CODE = 100;
    private MediaRecorder myRecorder;
    String path;
    private NoteModel noteModel;
    ListView lvAudio;

    public static final String TAG = "Audio";

    ArrayList<String> audioPaths = new ArrayList<>();

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
        RelativeLayout rlAudio = view.findViewById(R.id.rl_audio);
        TextView tvAudio = view.findViewById(R.id.txt_audio);
        final Chronometer chronometer = view.findViewById(R.id.chronometer);
        lvAudio = view.findViewById(R.id.lv_audio_notes);

        if(!checkAudioPermission())
            requestAudioPermission();

        if(noteModel != null){

//            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + noteModel.getId() + "/_1" + ".3gp";

            File root = Environment.getExternalStorageDirectory();
            File file = new File(root.getAbsolutePath() + "/" + noteModel.getId());
            if (!file.exists()) {
                file.mkdirs();
            }

        } else{
            rlAudio.setVisibility(View.GONE);
            tvAudio.setVisibility(View.VISIBLE);
        }


        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAudioPermission()){
                    setPath();
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

                getPaths();

                chronometer.stop();
                ivRecord.setVisibility(View.VISIBLE);
                ivStop.setVisibility(View.GONE);

            }
        });

    }

    private void setPath(){

        File root = Environment.getExternalStorageDirectory();
        File folder = new File(root.getAbsolutePath() + "/" + noteModel.getId());
        File files[] = folder.listFiles();
        if(files != null) {
            path = folder.getPath() + "/" + noteModel.getId() + "_" + files.length + ".3gp";
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    //here populate your list
                    Log.i(TAG, "onViewCreated: " + files[i]);
                }
            } else {
                //no file available
                Log.i(TAG, "onViewCreated: " + "no files");

            }
        } else{
            Log.i(TAG, "onViewCreated: null " );
        }
    }

    private void getPaths(){
        File root = Environment.getExternalStorageDirectory();
        File folder = new File(root.getAbsolutePath() + "/" + noteModel.getId());
        File files[] = folder.listFiles();
        if(files != null) {
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    //here populate your list
                    audioPaths.add(files[i].toString());
                    Log.i(TAG, "onViewCreated: getpaths " + files[i]);
                }
            } else {
                //no file available
                Log.i(TAG, "onViewCreated: getpaths " + "no files");

            }
        } else{
            Log.i(TAG, "onViewCreated: getpaths null " );
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext() , android.R.layout.simple_list_item_1 , audioPaths);
        lvAudio.setAdapter(arrayAdapter);
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
