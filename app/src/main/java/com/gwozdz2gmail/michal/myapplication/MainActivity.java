package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.content.Intent;
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
    private int visible = View.INVISIBLE;
    private static final String TAG = "AndroidCameraApi";
    private ImageButton takePictureButton, settingsButton, filtersButton, shareButton, languagesButton, creditsButton, bugButton;
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
        settingsButton = (ImageButton) findViewById(R.id.settings);
        filtersButton = (ImageButton) findViewById(R.id.filters);
        shareButton = (ImageButton) findViewById(R.id.share);
        languagesButton = (ImageButton) findViewById(R.id.languages);
        creditsButton = (ImageButton) findViewById(R.id.credits);
        bugButton = (ImageButton) findViewById(R.id.bugs);
    }

    private void initListeners() {
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera2API.takePicture();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible == View.INVISIBLE) {
                    visible = View.VISIBLE;
                } else {
                    visible = View.INVISIBLE;
                }

                filtersButton.setVisibility(visible);
                shareButton.setVisibility(visible);
                languagesButton.setVisibility(visible);
                creditsButton.setVisibility(visible);
                bugButton.setVisibility(visible);
            }
        });

        filtersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "FILTERS", Toast.LENGTH_LONG).show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "SHARE", Toast.LENGTH_LONG).show();
            }
        });

        languagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "LANGUAGES", Toast.LENGTH_LONG).show();
            }
        });

        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "CREDITS", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
            }
        });

        bugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "BUG REPORTS", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, BugReportActivity.class);
                startActivity(intent);
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

    /**
     * Shows popup menu.
     * @param view menu's owner
     */
    public void showPopup(View view){
        ProgramManager.showPopup(this, view);
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

