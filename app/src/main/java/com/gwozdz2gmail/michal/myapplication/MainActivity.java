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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends Activity{
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private ImageButton createPhotoButton = null;
    private ImageView image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createPhotoButton = (ImageButton) findViewById(R.id.createImageB);

        /*AdapterView.OnClickListener createPhoto = new View.OnClickListener() {
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
        }*/
        image = (ImageView) findViewById(R.id.imageView);
    }

    /**
     * Load photo from gallery.
     * @param view method's owner
     */
    public void load(View view){
        final String choosingOption = getResources().getString(R.string.choosing);
        final CharSequence[] options = {choosingOption, getResources().getString(R.string.cancel)};
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(R.string.selecting_photo);
        dialog.setItems(options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item){
                if(options[item].equals(choosingOption)){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
                    .Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }else dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                Log.w("path of image ", picturePath+"");
                image.setImageBitmap(thumbnail);
            }
        }
    }
}

