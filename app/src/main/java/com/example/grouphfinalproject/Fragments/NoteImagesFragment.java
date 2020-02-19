package com.example.grouphfinalproject.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.grouphfinalproject.Adapters.NoteImageAdapter;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NoteImagesFragment extends Fragment {

    private static final int IMAGE_REQUEST_CODE = 10;
    private final int CAMERA_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE = 2;
    public static final String TAG = "Note ImagesFrag";

    NoteModel noteModel;
    ImageView ivCapturedImage;
    private File directory;

    GridView gridView;
    ArrayList<String> imagesPath;

    public NoteImagesFragment(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton btnAddImage = view.findViewById(R.id.btn_add_image);
        gridView = view.findViewById(R.id.gridview_images);
        final RelativeLayout rlZoomImage = view.findViewById(R.id.rl_zoom_image);
        final RelativeLayout rlList = view.findViewById(R.id.rl_list);
        final ImageView ivZoom = view.findViewById(R.id.iv_full_image);


        if(!checkCameraPermission()){
            requestCameraPermission();
        }

        if(noteModel != null){
            File sdCard = Environment.getExternalStorageDirectory();
            directory = new File(sdCard.getAbsolutePath() + "/NotesImages/" + noteModel.getId());
            if(!directory.exists())
                directory.mkdirs();
            else
                showImagesInGridView();

            NoteImageAdapter imageAdapter = new NoteImageAdapter(getContext(), imagesPath);
            gridView.setAdapter(imageAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), "Image selected " + position, Toast.LENGTH_SHORT).show();
                    rlList.setClickable(false);
                    rlZoomImage.setVisibility(View.VISIBLE);

                    File imgFile = new File(imagesPath.get(position));
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivZoom.setImageBitmap(myBitmap);

                }
            });
        }

        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlZoomImage.setVisibility(View.GONE);
                rlList.setClickable(true);
            }
        });


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image_picker, null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final LinearLayout llImage, llButtons;
                llImage = dialogView.findViewById(R.id.ll_image);
                llButtons = dialogView.findViewById(R.id.ll_buttons);
                ivCapturedImage = dialogView.findViewById(R.id.iv_image_to_save);

                dialogView.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(checkCameraPermission()){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                llButtons.setVisibility(View.GONE);
                                llImage.setVisibility(View.VISIBLE);
                            }
                        } else{
                            requestCameraPermission();
                        }

                    }
                });
                dialogView.findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickFromGallery();
                        llButtons.setVisibility(View.GONE);
                        llImage.setVisibility(View.VISIBLE);

                    }
                });
                dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        llButtons.setVisibility(View.VISIBLE);
                        llImage.setVisibility(View.GONE);
                    }
                });

                dialogView.findViewById(R.id.btn_save_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storeToDevice();
                        alertDialog.dismiss();
//                        llButtons.setVisibility(View.VISIBLE);
//                        llImage.setVisibility(View.GONE);

                    }
                });

            }
        });
    }

    private boolean checkCameraPermission(){
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        int write_permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return (permissionState == PackageManager.PERMISSION_GRANTED) &&
                (read_permission == PackageManager.PERMISSION_GRANTED) &&
                (write_permission == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission(){
        requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == IMAGE_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    Log.i(TAG, "onActivityResult: + camera" + bitmap.toString());
                    ivCapturedImage.setImageBitmap(bitmap);
                    break;

                case GALLERY_REQUEST_CODE:
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        ivCapturedImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Log.i(TAG, "onActivityResult: + file path " + filePathColumn);
//
//                    Cursor cursor = getContentResolver().query(uri,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    Log.i(TAG, "onActivityResult: " + picturePath);
//                    cursor.close();
//
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    break;
            }

        }
    }


    private void storeToDevice(){
//        savedImageURL = MediaStore.Images.Media.insertImage(
//                getContentResolver(),
//                bitmap,
//                "Bird",
//                "Image of bird"
//        );


        BitmapDrawable draw = (BitmapDrawable) ivCapturedImage.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        FileOutputStream outStream = null;

        // Write to SD Card
        try {


//            String path = dir.getPath() + "/" + count + ".jpg";

//            File path = new File(dir.getPath() + "/" + count + ".jpg");
//            if(!path.exists())
//                path.createNewFile();

            File files[] = directory.listFiles();
            if(files != null){
                String fileName = String.format("image_%d_%d.png", noteModel.getId(), files.length);
                File outFile = new File(directory, fileName);
                outFile.createNewFile();
                Log.d(TAG, "onPictureTaken - wrote to " + outFile.getAbsolutePath());

                outStream = new FileOutputStream(outFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();

                Log.i(TAG, "onPictureTaken - wrote to " + outFile.getAbsolutePath());
            } else{
                Log.i(TAG, "storeToDevice: null directory");
            }

        } catch (FileNotFoundException e) {
            Log.i(TAG, "storeToDevice: FNF" );
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showImagesInGridView(){

        imagesPath = new ArrayList<>();
        File files[] = directory.listFiles();
        if(files != null){
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    //here populate your list
                    imagesPath.add(files[i].toString());
                    Log.i(TAG, "onViewCreated: getpaths " + files[i]);
                }
            } else {
                //no file available
                Log.i(TAG, "onViewCreated: getpaths " + "no files");

            }

        } else{
            Log.i(TAG, "showImagesInGridView: null directory");
        }
    }
}
