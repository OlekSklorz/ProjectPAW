package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity{
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private ImageButton createPhotoButton = null;
    private ImageView image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createPhotoButton = (ImageButton) findViewById(R.id.createImageB);

        AdapterView.OnClickListener createPhoto = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("o chuh");
            }
        };

        createPhotoButton.setOnClickListener((View.OnClickListener) createPhoto);

        try{
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }
        image = (ImageView) findViewById(R.id.imageView);
    }

    /**
     * Shows dialog with three options: saving, loading photo and cancel.
     * @param view method's owner
     */
    public void showFileActions(View view){
        final String choosingOption = getResources().getString(R.string.choosing),
                savingOption = getResources().getString(R.string.saving);
        final CharSequence[] options = {choosingOption, savingOption, getResources().getString(R.string.cancel)};
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(R.string.selecting_photo);
        dialog.setItems(options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item){
                if(options[item].equals(choosingOption)){
                    Intent loadingIntent = new Intent(MainActivity.this, LoadingPhotoActivity.class);
                    startActivity(loadingIntent);
                }else if(options[item].equals(savingOption)){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 2);
                }else dialog.dismiss();
            }
        });
        dialog.show();
    }
}

