package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity{
    private static final String TAG = "AndroidCameraApi";
    private ImageButton takePictureButton;
    protected static TextureView textureView;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Camera2API camera2API;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera2API = new Camera2API(MainActivity.this);

        initViewElements();
        initListeners();
    }

    private void initViewElements() {
        textureView = (TextureView) findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(textureListener);

        takePictureButton = (ImageButton) findViewById(R.id.btn_take_picture);
        ProgramManager.initSettingsButtons(this);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case(R.id.off_filter) :
                        camera2API.setFilter(0);
                    break;
                    case(R.id.mono_filter) :
                        camera2API.setFilter(1);
                    break;
                    case(R.id.negative_filter) :
                        camera2API.setFilter(2);
                    break;
                    case(R.id.solarize_filter) :
                        camera2API.setFilter(3);
                    break;
                    case(R.id.sepia_filter) :
                        camera2API.setFilter(4);
                    break;
                }
            }
        };

        ProgramManager.offButton.setOnClickListener(listener);
        ProgramManager.monoButton.setOnClickListener(listener);
        ProgramManager.negativeButton.setOnClickListener(listener);
        ProgramManager.solarizeButton.setOnClickListener(listener);
        ProgramManager.sepiaButton.setOnClickListener(listener);
    }

    private void initListeners() {
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera2API.takePicture();
            }
        });
    }

    /**
     * Loading photo.
     * @param view method's owner
     */
    public void load(View view){
        ProgramManager.showChooser(this, null);
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            camera2API.openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        camera2API.startBackgroundThread();
        if (textureView.isAvailable()) {
            camera2API.openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        camera2API.closeCamera();
        camera2API.stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}

