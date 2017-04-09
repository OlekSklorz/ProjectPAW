package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity{
    private static final String TAG = "AndroidCameraApi";
    private ImageButton takePictureButton, reverseCameraButton;
    protected static TextureView textureView;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Camera2API camera2API;
    private int index = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera2API = new Camera2API(MainActivity.this);
        initViewElements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera2API.startBackgroundThread();
        if (textureView.isAvailable()) {
            camera2API.openCamera(0);
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

    private void initViewElements() {
        initTextureView();
        initSettingsButtons();
        initTakePictureButton();
        initReverseCameraButton();
    }

    private void initTextureView() {
        textureView = (TextureView) findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(textureListener);
    }

    private void initSettingsButtons() {
        ProgramManager.initSettingsButtons(this);
        initFiltersListener();
    }

    private void initFiltersListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case(R.id.filter_0) :
                        camera2API.setFilter(0);
                        break;
                    case(R.id.filter_1) :
                        camera2API.setFilter(4);
                        break;
                    case(R.id.filter_2) :
                        camera2API.setFilter(1);
                        break;
                    case(R.id.filter_3) :
                        camera2API.setFilter(6);
                        break;
                    case(R.id.filter_4) :
                        camera2API.setFilter(2);
                        break;
                    case(R.id.filter_5) :
                        camera2API.setFilter(7);
                }
            }
        };

        ProgramManager.offButton.setOnClickListener(listener);
        ProgramManager.monoButton.setOnClickListener(listener);
        ProgramManager.negativeButton.setOnClickListener(listener);
        ProgramManager.whiteBeardButton.setOnClickListener(listener);
        ProgramManager.sepiaButton.setOnClickListener(listener);
        ProgramManager.blackBeardButton.setOnClickListener(listener);
    }

    private void initTakePictureButton() {
        takePictureButton = (ImageButton) findViewById(R.id.btn_take_picture);
        initTakePictureListener();
    }

    private void initTakePictureListener() {
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera2API.takePicture();
            }
        });
    }

    private void initReverseCameraButton() {
        reverseCameraButton = (ImageButton) findViewById(R.id.reverseCamera);
        initReverseCameraListener();
    }

    private void initReverseCameraListener() {
        reverseCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera2API.closeCamera();
                if(index == 0) {
                    index = 1;
                } else if(index == 1) {
                    index = 0;
                }
                camera2API.openCamera(index);
            }
        });
    }

    /**
     * Loading photo.
     * @param view method's owner
     */
    public void loadFileAction(View view){
        ProgramManager.showChooser(this, null);
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            camera2API.openCamera(0);
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

