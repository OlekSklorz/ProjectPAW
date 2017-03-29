package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends Activity{
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private ImageButton createPhotoButton = null;

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
    }

}

