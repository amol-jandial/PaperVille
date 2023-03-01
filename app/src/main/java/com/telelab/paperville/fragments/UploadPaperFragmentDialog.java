package com.telelab.paperville.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.telelab.paperville.DatabaseHandler;
import com.telelab.paperville.HelperMethods;
import com.telelab.paperville.R;
import com.telelab.paperville.models.Paper;

import java.util.ArrayList;

public class UploadPaperFragmentDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "UploadPaperFragmentDial";
    private AppCompatButton buttonAdd;
    private View view;
    private ArrayList<Uri> imageUris;
    public static final int REQUEST_ID_PERMISSIONS = 1;
    public static final int REQUEST_ID_PHOTOS = 2;
    private LinearLayout linearLayoutImagePreview;
    private AppCompatButton buttonUpload;
    private DatabaseHandler databaseHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.upload_paper_dialog, null);
        init();
        setListeners();
        builder.setView(view);
        return builder.create();
    }

    private void init(){
       buttonAdd = view.findViewById(R.id.button_add_images);
       linearLayoutImagePreview = view.findViewById(R.id.linear_layout_preview_images);
       buttonUpload = view.findViewById(R.id.button_upload_images);
       imageUris = new ArrayList<>();
    }

    private void setListeners() {
        buttonAdd.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    private void addImages(){
        if(allPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)){
            startCamera();
        }else{
            requestPermissions();
        }
    }

    private void requestPermissions(){
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(
                permissions, REQUEST_ID_PERMISSIONS);
    }

    private boolean allPermissionsGranted(String... permissions){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    private void startCamera(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_ID_PHOTOS);

    }

    private void showSelectedImages(){
        for(Uri imageUri : imageUris){
            ImageView image = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    HelperMethods.pxToDp(55, getResources()),
                    HelperMethods.pxToDp(55, getResources())
            );
            params.setMargins(
                    HelperMethods.pxToDp(8, getResources()),
                    0,
                    HelperMethods.pxToDp(8, getResources()),
                    0);
            image.setLayoutParams(params);
            image.setImageURI(imageUri);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            linearLayoutImagePreview.addView(image);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ID_PHOTOS){
            if(resultCode == Activity.RESULT_OK){
                if(null != data.getClipData()){
                    int imageCount = data.getClipData().getItemCount();
                    for(int i = 0; i < imageCount; i++){
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                    Log.d(TAG, "onActivityResult: "+imageUris);
                }else{
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    Log.d(TAG, "onActivityResult: "+imageUris);
                }
                if(!imageUris.isEmpty()){
                    buttonAdd.setVisibility(View.GONE);
                    showSelectedImages();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ID_PERMISSIONS) {
            if (allPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                startCamera();
            } else {
                Toast.makeText(getActivity(), "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add_images:
                addImages();
                break;

            case R.id.button_upload_images:
                if(!imageUris.isEmpty()){
                    databaseHandler = new DatabaseHandler(getContext());
                    Paper paper = new Paper("Calculus", "Minor1", false);
                    databaseHandler.uploadImages(imageUris, paper);
                }
        }
    }
}
